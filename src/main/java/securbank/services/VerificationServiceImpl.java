/**
 * 
 */
package securbank.services;

import java.util.UUID;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import securbank.dao.VerificationDao;
import securbank.models.User;
import securbank.models.Verification;

/**
 * @author Ayush Gupta
 *
 */
@Service("verificationService")
public class VerificationServiceImpl implements VerificationService {

	@Autowired
	VerificationDao verificationDao;
	
	/* (non-Javadoc)
	 * @see securbank.services.VerificationService#getUserByIdAndType(java.util.UUID, java.lang.String)
	 */
	@Override
	public User getUserByIdAndType(UUID id, String type) {
		return verificationDao.findUserByIdAndType(id, type);
	}

	/* (non-Javadoc)
	 * @see securbank.services.VerificationService#createVerificationCodeByType(java.lang.String)
	 */
	@Override
	public Verification createVerificationCodeByType(User user, String type) {
		Verification verification = new Verification();
		verification.setCreatedOn(LocalDateTime.now());
		if (type.equalsIgnoreCase("lock")) {
			verification.setExprireOn(LocalDateTime.now().plusYears(1));
		}
		else {
			verification.setExprireOn(LocalDateTime.now().plusDays(1));
		}
		verification.setUser(user);
		verification = verificationDao.save(verification);
		
		return verification;
	}
	
}
