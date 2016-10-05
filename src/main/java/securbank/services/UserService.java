/**
 * 
 */
package securbank.services;

import java.util.UUID;

import securbank.models.NewUserRequest;
import securbank.models.User;

/**
 * @author Ayush Gupta
 *
 */
public interface UserService {
	public User createExternalUser(User user);
	public User createInternalUser(User user);
	public NewUserRequest createUserRequest(NewUserRequest newUserRequest);
	public User getCurrentUser();
	public boolean verifyNewUser(UUID userId);
	public NewUserRequest getNewUserRequest(UUID newUserRequestId);
}
