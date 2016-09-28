package securbank.dao;

import securbank.models.Account;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

@Repository("accountDao")
public class AccountDaoImpl extends BaseDaoImpl<Account, UUID> implements AccountDao {
	
	@Autowired
	EntityManager entityManager;
	
	
	public AccountDaoImpl(){
		super(Account.class);
	}
	
	
	@Override
	public Account findByAccountNum(UUID accountNumber){
	
		
		try{
			String qu = "SELECT acc from Account acc where acc.accountNumber LIKE :accountNum";
			return (Account) this.entityManager.createQuery(qu)
					.setParameter("accountNum", accountNumber)
					.getSingleResult();
		}
		catch(NoResultException e){
			return null;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Account> findAll(){
		
		String qu = "SELECT acc from Account acc";
		return (List<Account>) this.entityManager.createQuery(qu).getResultList();
					
	
		
	}
	
	
	
	

}
