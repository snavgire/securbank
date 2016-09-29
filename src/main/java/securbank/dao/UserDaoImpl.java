package securbank.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import securbank.models.User;

/**
 * @author Ayush Gupta
 *
 */
@Repository("userDao")
public class UserDaoImpl extends BaseDaoImpl<User, UUID> implements UserDao {
	@Autowired
	EntityManager entityManager; 
	
	/**
     * Returns user for the given Id.
     * 
     * @param user
     *            The username or email to query db
     * @return User
     */
	public UserDaoImpl() {
		super(User.class);
	}
	
	@Override
	public User findByUsernameOrEmail(String user) {
		try {
			return (User) this.entityManager.createQuery("SELECT user from User user where (user.username = :username OR user.email = :email) AND user.active = 1")
					.setParameter("username", user)
					.setParameter("email", user)
					.getSingleResult();
		}
		catch(NoResultException e) {
			// returns null if no user if found
			return null;
		}
	}

	/**
     * Returns list of all users in the tables
     * 
     * @return users
     */
	@SuppressWarnings("unchecked")
	@Override
	public List<User> findAll() {
		return (List<User>) this.entityManager.createQuery("SELECT user from User user")
											.getResultList();
	}
}
