/**
 * 
 */
package securbank.services;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import securbank.dao.ModificationRequestDao;
import securbank.dao.NewUserRequestDao;
import securbank.dao.UserDao;
import securbank.models.Account;
import securbank.models.ModificationRequest;
import securbank.models.NewUserRequest;
import securbank.models.User;

/**
 * @author Ayush Gupta
 *
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private NewUserRequestDao newUserRequestDao;
	
	@Autowired
	private ModificationRequestDao modificationRequestDao;
	
	@Autowired 
	private AccountService accountService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private Environment env;
	
	private SimpleMailMessage message;
	
	/**
     * Creates new user
     * 
     * @param user
     *            The user to be created
     * @return user
     */
	@Override
	public User createExternalUser(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		user.setCreatedOn(LocalDateTime.now());
		user.setActive(true);
		
		// Creates new checking account
		Account account = new Account();
		account.setUser(user);
		account.setType("checking");
		account.setBalance(0.0);
		account = accountService.createAccount(account);
		
		return account.getUser();
	}
	
	/**
     * Creates new user
     * 
     * @param user
     *            The user to be created
     * @return user
     */
	@Override
	public User createInternalUser(User user) {
		NewUserRequest newUserRequest = new NewUserRequest();
		
		// verify if request exists
		newUserRequest = newUserRequestDao.findByEmailAndRole(user.getEmail(), user.getRole()); 
		if (newUserRequest == null) {
			return null;
		}
		
		// Deactivates request
		newUserRequest.setActive(false);
		newUserRequestDao.update(newUserRequest);
		
		// creates new user
		user.setPassword(encoder.encode(user.getPassword()));
		user.setCreatedOn(LocalDateTime.now());
		user.setActive(true);
		
		return userDao.save(user);
	}
	
	/**
     * Get current logged in user
     * 
     * @return user
     */
	@Override
	public User getCurrentUser() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user == null) {
			return null;
		}
		
		// gets latest object from db
		user = userDao.findById(user.getUserId());
		if (user == null) {
			return null;
		}
		
		return user;
	}

	/**
     * Creates new user request
     * 
     * @param newUserRequest
     *            The user request to be created
     * @return newUserRequest
     */
	@Override
	public NewUserRequest createUserRequest(NewUserRequest newUserRequest) {
		newUserRequest.setCreatedOn(LocalDateTime.now());
		newUserRequest.setExpireOn(LocalDateTime.now().plusDays(1));
		newUserRequest.setActive(true);
		newUserRequest = newUserRequestDao.save(newUserRequest);
		
		//setup up email message
		message = new SimpleMailMessage();
		message.setText(env.getProperty("user.verification.body").replace(":id:",newUserRequest.getNewUserRequestId().toString()));
		message.setSubject(env.getProperty("user.verification.subject"));
		message.setTo(newUserRequest.getEmail());
		
		// send email
		if (emailService.sendEmail(message) == false) {
			// Deactivate request if email fails
			newUserRequest.setActive(false);
			newUserRequestDao.update(newUserRequest);
			
			return null;
		};
		
		return newUserRequest;
	}
	
	/**
     * Get new user request and deactivates it
     * @param newUserRequestId
     *            The user request id to be retrieved
     * @return newUserRequest
     */
	@Override
	public NewUserRequest getNewUserRequest(UUID newUserRequestId) {
		NewUserRequest newUserRequest = newUserRequestDao.findById(newUserRequestId);
		if (newUserRequest == null) {
			return null;
		}
	
		return newUserRequest;
	}

	/**
     * Creates new user request
     * 
     * @param user
     *            The modified user to be create 
     * @return modificationRequest
     */
	@Override
	public ModificationRequest createModificationRequest(ModificationRequest request) {
		User user = getCurrentUser();
		List<ModificationRequest> requests = modificationRequestDao.findAllbyUser(user);
		
		// Deactivate all active request 
		for (ModificationRequest activeRequest : requests) {
			activeRequest.setActive(false);
			modificationRequestDao.update(activeRequest);
		}
		request.setUser(user);
		request.setPassword(user.getPassword());
		request.setActive(true);
		request.setCreatedOn(LocalDateTime.now());
		request.setStatus("pending");
		modificationRequestDao.save(request);
		
		return request;
	}

	/**
     * Approves user request
     * 
     * @param requestId
     *            The id of the request to be approved 
     * @return modificationRequest
     */
	@Override
	public ModificationRequest approveModificationRequest(UUID requestId) {
		ModificationRequest request = modificationRequestDao.findById(requestId);
		if (request == null) {
			return null;
		}
		
		User user = request.getUser();
		
		// If email has been taken
		if (userDao.emailExists(request.getEmail()) || newUserRequestDao.emailExists(request.getEmail())) {
			
			// Sends an email if requested change is not acceptable
			SimpleMailMessage message = new SimpleMailMessage();
			message.setText(env.getProperty("modification.request.failure.body"));
			message.setSubject(env.getProperty("modification.request.failure.subject"));
			message.setTo(user.getEmail());
			emailService.sendEmail(message);
			
			// update request
			request.setActive(false);
			request.setStatus("rejected");
			request.setModifiedOn(LocalDateTime.now());
			modificationRequestDao.update(request);
			
			return null;
		}
		
		// Update User
		user.setUsername(request.getUsername());
		user.setFirstName(request.getFirstName());
		user.setMiddleName(request.getMiddleName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());
		user.setPhone(request.getPhone());
		user.setAddressLine1(request.getAddressLine1());
		user.setAddressLine2(request.getAddressLine2());
		user.setCity(request.getCity());
		user.setState(request.getState());
		user.setZip(request.getZip());
		user.setPassword(request.getPassword());
		user.setModifiedOn(LocalDateTime.now());
		userDao.update(user);
		
		// Update request
		request.setActive(false);
		request.setModifiedOn(LocalDateTime.now());
		request.setStatus("approved");
		request = modificationRequestDao.update(request);
		
		return request;
	}
	
	/**
     * Rejects user request
     * 
     * @param requestId
     *            The id of the request to be approved 
     * @return modificationRequest
     */
	@Override
	public ModificationRequest rejectModificationRequest(UUID requestId) {
		ModificationRequest request = modificationRequestDao.findById(requestId);
		User user = request.getUser();
			
		// Sends an email if request is rejected
		SimpleMailMessage message = new SimpleMailMessage();
		message.setText(env.getProperty("modification.request.reject.body"));
		message.setSubject(env.getProperty("modification.request.reject.subject"));
		message.setTo(user.getEmail());
		emailService.sendEmail(message);
	
		// update request
		request.setActive(false);
		request.setStatus("rejected");
		request.setModifiedOn(LocalDateTime.now());
		request.setApprovedBy(getCurrentUser());
		request = modificationRequestDao.update(request);
		
		return request;
	}
	
	/**
     * Get all pending user request
     * 
     * @return List<modificationRequest>
     */
	public List<ModificationRequest> getAllPendingUserModificationRequest() {
		return modificationRequestDao.findAllbyStatus("pending");
	}
	
	/**
     * Get request by Id
     * 
     * @param requestId
     *            The id of the request to be retrieved 
     * @return modificationRequest
     */
	public ModificationRequest getModificationRequest(UUID requestId) {
		return modificationRequestDao.findById(requestId);
	}
}
