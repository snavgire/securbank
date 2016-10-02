/**
 * 
 */
package securbank.services;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	
	@Value("${application.url}")
	private String url;
	
	@Value("${user.verification.body}")
	private String verificationBody;
	
	@Value("${user.verification.subject}")
	private String verificationSubject;
	
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
	      
		return userDao.findById(user.getUserId());
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
		message.setText(verificationBody.replace(":id:",newUserRequest.getNewUserRequestId().toString()));
		message.setSubject(verificationSubject);
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
     * Creates new user request
     * 
     * @param user
     *            The modified user to be create 
     * @return modificationRequest
     */
	@Override
	public ModificationRequest approveModificationRequest(UUID requestId) {
		ModificationRequest request = modificationRequestDao.findById(requestId);
		User user = request.getUser();
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
		userDao.update(user);
		
		user.setModifiedOn(LocalDateTime.now());
		request.setActive(false);
		request.setCreatedOn(LocalDateTime.now());
		request.setStatus("pending");
		modificationRequestDao.save(request);
		
		return request;
	}

}
