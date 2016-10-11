package securbank.services;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private SimpleMailMessage message;
	
	@Autowired
	private Environment env;
	
	final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	/**
     * Creates new user
     * 
     * @param user
     *            The user to be created
     * @return user
     */
	@Override
	public User createExternalUser(User user) {
		logger.info("Creating new external user");
		user.setPassword(encoder.encode(user.getPassword()));
		user.setCreatedOn(LocalDateTime.now());
		user.setActive(false);
		user.setType("external");
		user = userDao.save(user);
		
		//setup up email message
		message = new SimpleMailMessage();
		message.setText(env.getProperty("external.user.verification.body").replace(":id:",user.getUserId().toString()));
		message.setSubject(env.getProperty("external.user.verification.subject"));
		message.setTo(user.getEmail());
		emailService.sendEmail(message);
		
		return user;
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
			logger.info("Invalid request for new internal user");
			return null;
		}
		
		// Deactivates request
		newUserRequest.setActive(false);
		newUserRequestDao.update(newUserRequest);
		
		logger.info("Creating new internal user");
		
		// creates new user
		user.setType("internal");
		user.setPassword(encoder.encode(user.getPassword()));
		user.setCreatedOn(LocalDateTime.now());
		user.setActive(true);
		
		return userDao.save(user);
	}
	
	/**
     * Verify new user
     * @param userId
     * 			User id to be verified
     * @return user
     */
	@Override
	public boolean verifyNewUser(UUID userId) {
		User user = userDao.findById(userId);
		if (user == null || userDao.emailExists(user.getEmail()) || userDao.phoneExists(user.getPhone()) || userDao.usernameExists(user.getUsername())) {
			logger.info("Verification for existing email, phone or username");
			return false;
		}
		
		if (user.getActive() == true) {
			logger.info("Verification for active user");
			return true;
		}
		
		logger.info("Verifying and create account for new external user");
		
		user.setActive(true);
		user = userDao.update(user);

		// Creates new checking account
		Account account = new Account();
		account.setUser(user);
		account.setType("checking");
		account.setBalance(0.0);
		account = accountService.createAccount(account);
				
		if (user == null) {
			return false;
		}
		
		return true;
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
		logger.info("Getting current logged in user");
		return userDao.findById(user.getUserId());
	}

	/**
     * Get all users by type
     *
	 * @return List<user>
     */
	@Override
	public List<User> getUsersByType(String type) {
		List<User> users = userDao.findAllByType(type);
		logger.info("Getting users by type");
		
		return users;
	}
	
	/**
     * Get all users by id
     *
	 * @return user
     */
	@Override
	public User getUserByIdAndActive(UUID id) {
		User user = userDao.findById(id);
		if (user == null || user.getActive() == false) {
			return null;
		}
		
		logger.info("Getting user by id");
		
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
	public NewUserRequest createNewUserRequest(NewUserRequest newUserRequest) {
		newUserRequest.setCreatedOn(LocalDateTime.now());
		newUserRequest.setExpireOn(LocalDateTime.now().plusDays(1));
		newUserRequest.setActive(true);
		newUserRequest = newUserRequestDao.save(newUserRequest);
		
		logger.info("Creating new internal user request");
		
		//setup up email message
		message = new SimpleMailMessage();
		message.setText(env.getProperty("internal.user.verification.body").replace(":id:",newUserRequest.getNewUserRequestId().toString()));
		message.setSubject(env.getProperty("internal.user.verification.subject"));
		message.setTo(newUserRequest.getEmail());
		
		// send email
		if (emailService.sendEmail(message) == false) {
			// Deactivate request if email fails
			newUserRequest.setActive(false);
			newUserRequestDao.update(newUserRequest);
			logger.warn("Email message failed");
			
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
			logger.info("Request for getting new user request with invalid id");
			return null;
		}
		logger.info("Getting new user request by id");
		return newUserRequest;
	}

	/**
     * Creates external user modification request
     * 
     * @param request
     *            The modification request to be create 
     * @return modificationRequest
     */
	@Override
	public ModificationRequest createInternalModificationRequest(ModificationRequest request) {
		User user = getCurrentUser();
		if (user == null) {
			return null;
		}
		List<ModificationRequest> requests = modificationRequestDao.findAllbyUser(user);
		
		// Deactivate all active request 
		for (ModificationRequest activeRequest : requests) {
			activeRequest.setActive(false);
			modificationRequestDao.update(activeRequest);
		}
		
		if (!request.getEmail().equals(user.getEmail())) {
			request.setStatus("waiting");
		}
		else {
			request.setStatus("pending");
		}
		
		request.setUser(user);
		request.setPassword(user.getPassword());
		request.setActive(true);
		request.setCreatedOn(LocalDateTime.now());
		request.setUserType("internal");
		request = modificationRequestDao.save(request);
		
		if (!request.getEmail().equals(user.getEmail())) {
			message = new SimpleMailMessage(); 
			message.setText(env.getProperty("modification.request.verify.body").replace(":id:", request.getModificationRequestId().toString()));
			message.setSubject(env.getProperty("modification.request.verify.subject"));
			message.setTo(request.getEmail());
			emailService.sendEmail(message);
		}
		
		return request;
	}

	/**
     * Creates external user modification request
     * 
     * @param request
     *            The modification request to be create 
     * @return modificationRequest
     */
	@Override
	public ModificationRequest createExternalModificationRequest(ModificationRequest request) {
		User user = getCurrentUser();
		if (user == null) {
			return null;
		}
		
		List<ModificationRequest> requests = modificationRequestDao.findAllbyUser(user);
		
		// Deactivate all active request 
		for (ModificationRequest activeRequest : requests) {
			activeRequest.setActive(false);
			modificationRequestDao.update(activeRequest);
		}
		
		if (!request.getEmail().equals(user.getEmail())) {
			request.setStatus("waiting");
		}
		else {
			request.setStatus("pending");
		}
		request.setUser(user);
		request.setPassword(user.getPassword());
		request.setActive(true);
		request.setCreatedOn(LocalDateTime.now());
		request.setUserType("external");
		request.setRole(user.getRole());
		request = modificationRequestDao.save(request);
		
		if (!request.getEmail().equals(user.getEmail())) {
			message = new SimpleMailMessage();
			message.setText(env.getProperty("modification.request.verify.body").replace(":id:", request.getModificationRequestId().toString()));
			message.setSubject(env.getProperty("modification.request.verify.subject"));
			message.setTo(request.getEmail());
			emailService.sendEmail(message);
		}
				
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
	public ModificationRequest approveModificationRequest(ModificationRequest request) {
		User user = request.getUser();
		
		// If email has been taken
		if ((!request.getEmail().equals(user.getEmail()) && (userDao.emailExists(request.getEmail()) || newUserRequestDao.emailExists(request.getEmail())))
				|| (!request.getPhone().equals(user.getPhone()) && userDao.phoneExists(request.getPhone()))) {
			// Sends an email if email and phone clash with existing users
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
		if (request.getUserType().equals("internal")) {
			user.setRole(request.getRole());
		}
		user.setModifiedOn(LocalDateTime.now());
		userDao.update(user);
		
		// Update request
		request.setActive(false);
		request.setModifiedOn(LocalDateTime.now());
		request.setApprovedBy(getCurrentUser());
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
	public ModificationRequest rejectModificationRequest(ModificationRequest request) {
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
     * Get all pending internal user request
     * 
     * @param type
     *            The type of user 
     * @param status
     *            The status of request 
     * @return List<modificationRequest>
     */
	public List<ModificationRequest> getModificationRequests(String status, String type) {
		return modificationRequestDao.findAllbyStatusAndUserType(status, type);
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
	
	/**
     * Verify and update request to pending
     * 
     * @param status
     *            The status of the request 
     * @param requestId
     *            The id of the request to be verified 
     * @return boolean
     */
	public boolean verifyModificationRequest(String status, UUID requestId) {
		ModificationRequest request = modificationRequestDao.findById(requestId);
		if (request == null) {
			return false;
		}
		if (!request.getStatus().equals(status)) {
			return false;
		}
	
		request.setStatus("pending");
		modificationRequestDao.update(request);
		
		return true;
	}
}
