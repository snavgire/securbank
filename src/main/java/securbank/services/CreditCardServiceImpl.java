package securbank.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import securbank.dao.CreditCardDao;
import securbank.models.Account;
import securbank.models.CreditCard;

@Service("creditCardService")
@Transactional
public class CreditCardServiceImpl implements CreditCardService {
	
	/**
	 * How many times per year the interest is compounded. At 365,
	 * the interest is compounded daily.
	 */
	private static final double COMPOUNDS_PER_YEAR = 365d;
	
	@Autowired
	private CreditCardDao creditCardDao;

	@Override
	public CreditCard createCreditCard(Account account, double apr, double maxLimit) {
		CreditCard existingCc = creditCardDao.findByAccountNumber(account.getAccountNumber());
		if (existingCc != null) {
			return null;
		}
		CreditCard cc = new CreditCard();
		cc.setAccount(account);
		cc.setApr(apr);
		cc.setMaxLimit(maxLimit);
		cc.setBalance(0d);
		cc.setActive(false);
		return creditCardDao.save(cc);
	}

	@Override
	public double generateInterest(CreditCard cc) {
		return cc.getBalance() * cc.getApr() / COMPOUNDS_PER_YEAR;
	}

	@Override
	public CreditCard getCreditCardDetails(Account account) {
		return creditCardDao.findByAccountNumber(account.getAccountNumber());
	}

}
