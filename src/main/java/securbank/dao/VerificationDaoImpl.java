package securbank.dao;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import securbank.models.User;
import securbank.models.Verification;


/**
 * 
 * @author Ayush Gupta
 *
 */
@Repository("verificationDao")
public class VerificationDaoImpl extends BaseDaoImpl<Verification, UUID> implements VerificationDao {
	@Autowired
	EntityManager entityManager;
	
	public VerificationDaoImpl(){
		super(Verification.class);
	}

	/**
     * Returns user for the valid Id and type.
     * 
     * @param id
     *            The id to query
     * @param type
     *            The type of code
     * @return User
     */
	@Override
	public User findUserByIdAndType(UUID id, String type) {
		try {
			return this.entityManager.createQuery("SELECT verification.user from Verification verification " +
					"WHERE verification.verificationId = :id " +
					"AND verification.type = :type" + 
					"AND verification.expiredOn > :now", User.class)
					.setParameter("id", id)
					.setParameter("type", type)
					.setParameter("now", LocalDateTime.now())
					.getSingleResult();	
		}
		catch(NoResultException e) {
			return null;
		}
	}
}
