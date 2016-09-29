package securbank.dao;

import securbank.models.User;

import java.util.List;
import java.util.UUID;

/**
 * @author Ayush Gupta
 *
 */
public interface UserDao extends BaseDao<User, UUID> {
	public List<User> findAll();
	public User findByUsernameOrEmail(String user);
}
