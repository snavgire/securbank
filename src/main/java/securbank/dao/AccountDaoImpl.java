package securbank.dao;

import securbank.models.Account;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 * @author Madhu Illuri
 *
 */

@Repository("accountDao")
public class AccountDaoImpl extends BaseDaoImpl<Account, UUID> implements AccountDao {
	
	@Autowired
	EntityManager entityManager;
	
	
	public AccountDaoImpl(){
		super(Account.class);
	}
	
	/**
	* returns account for a given account number
	* @param accountNumber
	* @return account
	*/
	
	@Override
	public Account findByAccountNum(UUID accountNumber){
	
		
		try{
			String query = "SELECT acc from Account acc where acc.accountNumber = :accountNum";
			return (Account) this.entityManager.createQuery(query)
					.setParameter("accountNum", accountNumber)
					.getSingleResult();
		}
		catch(NoResultException e){
			return null;
		}
	}
	
	/**
	* returns list of all the accounts 
	* @return accounts
	*/
	@SuppressWarnings("unchecked")
	@Override
	public List<Account> findAll(){
		
		String query = "SELECT acc from Account acc";
		return (List<Account>) this.entityManager.createQuery(query).getResultList();
					
	
		
	}
	
	
	
	

}
