package securbank.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.joda.time.LocalDateTime;

import securbank.models.User;
import securbank.dao.UserDao;

/**
 * @author Ayush Gupta
 *
 */
@Service("authenticationService")
public class AuthenticationServiceImpl implements AuthenticationService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired 
	private PasswordEncoder encoder;
	
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
		
		if (encoder.encode(password).equals(user.getPassword())) {
			return user;
		};
		
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
		user = userDao.update(user);
		if (user == null) {
			return null;
		}
		
		return user;
	}
	
}
