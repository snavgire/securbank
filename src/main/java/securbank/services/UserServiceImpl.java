/**
 * 
 */
package securbank.services;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import securbank.dao.NewUserRequestDao;
import securbank.dao.UserDao;
import securbank.models.Account;
import securbank.models.ChangePasswordRequest;
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
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (username == null) {
			return null;
		}
		
		logger.info("Getting current logged in user");
		return userDao.findByUsernameOrEmail(username);
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
	public NewUserRequest createUserRequest(NewUserRequest newUserRequest) {
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

	@Override
	public boolean verifyCurrentPassword(User user, String password) {
		if (BCrypt.checkpw(password, user.getPassword()))
			return true;
	
		return false;
	}
	
	@Override
	public User changeUserPassword(User user, ChangePasswordRequest model){
		user.setPassword(encoder.encode(model.getNewPassword()));			
		userDao.update(user);
		
		return user;			
	}
}
