package securbank.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import securbank.dao.CreditCardDao;
import securbank.models.Account;
import securbank.models.CreditCard;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreditCardServiceTest {
	@Mock
	private CreditCardDao creditCardDao;

	@InjectMocks
	private CreditCardServiceImpl creditCardService;

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void createCreditCard_savesIfAccountHasNoActiveCreditCard() {
		long testAccountNumber = 5l;
		double apr = 0.15d;
		double maxLimit = 5000d;
		Account account = new Account();
		account.setAccountNumber(testAccountNumber);
		Mockito.when(creditCardDao.findByAccountNumber(testAccountNumber)).thenReturn(null);
		creditCardService.createCreditCard(account, apr, maxLimit);
		Mockito.verify(creditCardDao).save(Mockito.any());
	}

	@Test
	public void createCreditCard_doesNothingIfAccountHasActiveCreditCard() {
		long testAccountNumber = 5l;
		double apr = 0.15d;
		double maxLimit = 5000d;
		Account account = new Account();
		account.setAccountNumber(testAccountNumber);
		Mockito.when(creditCardDao.findByAccountNumber(testAccountNumber)).thenReturn(new CreditCard());
		creditCardService.createCreditCard(account, apr, maxLimit);
		Mockito.verify(creditCardDao, Mockito.never()).save(Mockito.any());
	}
	
	@Test
	public void generateInterest() {
		CreditCard cc = new CreditCard();
		cc.setApr(0.10d);
		cc.setBalance(730d);
		Assert.assertEquals(0.20d, creditCardService.generateInterest(cc), 0.01d);
	}
	
	@Test
	public void getCreditCardDetails_returnsCardIfActiveCardOnAccount() {
		long testAccountNumber = 5l;
		Account account = new Account();
		account.setAccountNumber(testAccountNumber);
		CreditCard cc1 = new CreditCard();
		Mockito.when(creditCardDao.findByAccountNumber(testAccountNumber)).thenReturn(cc1);
		CreditCard cc2 = creditCardService.getCreditCardDetails(account);
		Assert.assertSame(cc1, cc2);
	}
	
	@Test
	public void getCreditCardDetails_returnsNullIfNoActiveCardOnAccount() {
		long testAccountNumber = 5l;
		Account account = new Account();
		account.setAccountNumber(testAccountNumber);
		Mockito.when(creditCardDao.findByAccountNumber(testAccountNumber)).thenReturn(null);
		CreditCard cc = creditCardService.getCreditCardDetails(account);
		Assert.assertNull(cc);
	}
}
