package securbank.services;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import securbank.dao.UserDao;
import securbank.models.LoginAttempt;
import securbank.models.User;

/**
 * @author Ayush Gupta
 *
 */
@Transactional
@Service("authenticationService")
public class AuthenticationServiceImpl implements AuthenticationService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired 
	private PasswordEncoder encoder;
	
	@Autowired
	private EmailService emailService;
	
	private SimpleMailMessage message;
	
	@Autowired
	private Environment env;
	
	
	
	
	/**
     * Verify the username and password for current user
     * 
     * @param usernane
     *            The username or email of user
     * @param password
     *            The password of user
     * @return user
     */
	@Override
	public User verifyUser(String username, String password) {
		User user = userDao.findByUsernameOrEmail(username);
		if (user == null) {
			return null;
		}
		
		if (!BCrypt.checkpw(password, user.getPassword())) {
			//User found-Incorrect password, counter incremented 
			LoginAttempt attempt= user.getLoginAttempt();
			attempt.setCounter(attempt.getCounter() + 1);
			if(attempt.getCounter() == 3){
				user.setActive(false);
				//Send email message
				message = new SimpleMailMessage();
				message.setText(env.getProperty("account.reactivate.body").replace(":id:",user.getUserId().toString()));
				message.setSubject(env.getProperty("account.reactivate.subject"));
				message.setTo(user.getEmail());
				emailService.sendEmail(message);
			}
			user.setLoginAttempt(attempt);
			userDao.update(user);
			return null;
		}
		
		return user;
	}
	
	
	/**
     * Updates the last login time of user
     * 
     * @param user
     *            The User object of user
     * @return user
     */
	@Override
	public User updateLoginTime(User user) {
		user.setLastLogin(LocalDateTime.now());
		//Login Successful, LoginAttempt counter reset to 0
		LoginAttempt attempt= user.getLoginAttempt();
		attempt.setCounter(0);
		attempt.setLastUpdated(LocalDateTime.now());
		user.setLoginAttempt(attempt);
		
		user = userDao.update(user);
		if (user == null) {
			return null;
		}
		
		return user;
	}
	
}
