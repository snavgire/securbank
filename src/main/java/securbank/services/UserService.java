/**
 * 
 */
package securbank.services;

import java.util.List;
import java.util.UUID;

import securbank.models.ModificationRequest;
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
	public NewUserRequest getNewUserRequest(UUID newUserRequestId);
	public ModificationRequest createInternalModificationRequest(ModificationRequest request);
	public ModificationRequest createExternalModificationRequest(ModificationRequest request);
	public ModificationRequest approveModificationRequest(ModificationRequest request);
	public ModificationRequest rejectModificationRequest(ModificationRequest requestId);
	public List<ModificationRequest> getAllPendingModificationRequest(String type);
	public ModificationRequest getModificationRequest(UUID requestId);
}
