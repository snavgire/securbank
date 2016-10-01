/**
 * 
 */
package securbank.services;

import securbank.models.User;

/**
 * @author Ayush Gupta
 *
 */
public interface UserService {
	public User createExternalUser(User user);
	public User getCurrentUser();
}
