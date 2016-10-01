/**
 * 
 */
package securbank.services;

import javax.transaction.Transactional;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import securbank.dao.UserDao;
import securbank.models.Account;
import securbank.models.User;

/**
 * @author Ayush Gupta
 *
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;
	
	@Autowired 
	private AccountService accountService;
	
	@Autowired
	private PasswordEncoder encoder;
	
	/**
     * Creates new user
     * 
     * @param user
     *            The user to be created
     * @return user
     */
	@Override
	public User createInternalUser(User user) {
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
     * Get current logged in user
     * 
     * @return user
     */
	@Override
	public User getCurrentUser() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	      
		return userDao.findById(user.getUserId());
	}
	
}
