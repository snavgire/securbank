package securbank.services;

import java.util.List;
import java.util.Set;

import securbank.models.Account;
import securbank.models.Transaction;
import javax.transaction.Transactional;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import securbank.dao.AccountDao;
import securbank.dao.AccountDaoImpl;
import securbank.dao.TransactionDao;
import securbank.models.Transfer;
import securbank.models.User;

/**
 * @author Mitikaa
 *
 */

@Service("transactionService")
@Transactional
public class TransactionServiceImpl implements TransactionService{

	@Autowired
	private TransactionDao transactionDao;
	
	@Autowired
	private AccountDao accountDao;
	
	@Autowired 
	private AccountService accountService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
	
	final static Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

	/**
     * Initiates new debit transaction
     * 
     * @param transaction
     *            The transaction to be initiated
     * @return transaction
     */
	@Override
	public Transaction initiateDebit(Transaction transaction) {
		logger.info("Initiating new debit request");
		Account account = accountService.accountExists((transaction.getAccount()).getAccountNumber());
		if(account.getActive()==true){
			//transaction is valid if account exists
			transaction.setApprovalStatus("Pending");
			transaction.setType("Debit");
			transaction.setCreatedOn(LocalDateTime.now());
			transaction.setOldBalance(0.0);
			transaction.setNewBalance(0.0);
			transaction.setActive(false);
			transaction = transactionDao.save(transaction);
		}
		else {
			transaction.setApprovalStatus("Denied");
			transaction.setType("Debit");
			transaction.setCreatedOn(LocalDateTime.now());
			transaction.setOldBalance(0.0);
			transaction.setNewBalance(0.0);
			transaction.setActive(false);
			transaction = transactionDao.save(transaction);
		}
		return transaction;
	}

	/**
     * Initiates new credit transaction
     * 
     * @param transaction
     *            The transaction to be initiated
     * @return transaction
     * */
	@Override
	public Transaction initiateCredit(Transaction transaction) {
		logger.info("Initiating new credit request");
		Account account = accountService.accountExists((transaction.getAccount()).getAccountNumber());
		if(account.getActive()==true){
			transaction.setApprovalStatus("Pending");
			transaction.setType("Credit");
			transaction.setCreatedOn(LocalDateTime.now());
			transaction.setOldBalance(0.0);
			transaction.setNewBalance(0.0);
			transaction.setActive(false);
			transaction = transactionDao.save(transaction);
		}
		else {
			transaction.setApprovalStatus("Denied");
			transaction.setType("Credit");
			transaction.setCreatedOn(LocalDateTime.now());
			transaction.setOldBalance(0.0);
			transaction.setNewBalance(0.0);
			transaction.setActive(false);
			transaction = transactionDao.save(transaction);
		}
		return transaction;
	}

	/**
     * Initiates new transfer transaction
     * 
     * @param transfer
     *            The transfer to be initiated
     * @return transaction
     * */
	@Override
	public Transaction initiateTransfer(Transfer transfer) {
		logger.info("Initiating new transfer request");
		return null;
	}

	/**
     * Approves new transaction
     * 
     * @param transaction
     *            The transaction to be approved
     * @return transaction
     * */
	@Override
	public Transaction approveTransaction(Transaction transaction) {
		logger.info("Approving transaction request");
		Account account = transaction.getAccount();
		Double oldBalance = account.getBalance();
		Double newBalance = 0.0;
		if(transaction.getType()=="Credit"){
			newBalance = oldBalance + transaction.getAmount();
		}
		else {
			newBalance = oldBalance - transaction.getAmount();
		}
		transaction.setApprovalStatus("Approved");
		transaction.setOldBalance(oldBalance);
		transaction.setNewBalance(newBalance);
		account.setBalance(newBalance);
		transaction.setModifiedOn(LocalDateTime.now());
		//transaction.setModifiedBy(userService.getCurrentUser());
		transaction.setActive(true);
		transaction = transactionDao.update(transaction);
		account = accountDao.update(account);
		return transaction;
	}

	/**
     * Approves new transfer
     * 
     * @param transfer
     *            The transfer to be approved
     * @return transaction
     * */
	@Override
	public Transaction approveTransfer(Transfer transfer) {
		logger.info("Approving transfer request");
		return null;
	}

	/**
     * Declines new transaction
     * 
     * @param transaction
     *            The transaction to be declined
     * @return transaction
     * */
	@Override
	public Transaction declineTransaction(Transaction transaction) {
		logger.info("Declining transaction request");
		transaction.setApprovalStatus("Declined");
		transaction.setModifiedOn(LocalDateTime.now());
		//transaction.setModifiedBy(modifiedBy);
		transaction.setActive(false);
		transaction = transactionDao.update(transaction);
		return transaction;
	}

	/**
     * Declines new transfer
     * 
     * @param transfer
     *            The transfer to be declined
     * @return transaction
     * */
	@Override
	public Transaction declineTransaction(Transfer transfer) {
		logger.info("Declining transfer request");
		return null;
	}

	/**
     * Fetches pending transactions by account number
     * 
     * @param accountNumber
     *            The list of pending transactions
     * @return list of transactions
     * */
	@Override
	public List<Transaction> getPendingTransactionsByAccountNumber(String accountNumber) {
		logger.info("Getting pending transactions by account number");
		if(accountNumber==null){
			return null;
		}
		List<Transaction> transactions = transactionDao.findPendingByAccount(accountNumber);
		return transactions;
	}

	/**
     * Fetches pending transactions by account number and account type
     * 
     * @param accountNumber
     * @param accountType
     *            The list of pending transactions
     * @return list of transactions
     * */
	@Override
	public List<Transaction> getPendingTransactionsByType(String accountNumber, String accountType) {
		logger.info("Getting pending transactions by account number and account type");
		if(accountNumber==null||accountType==null){
			return null;
		}
		List<Transaction> transactions = transactionDao.findPendingByAccountAndType(accountNumber, accountType);
		return transactions;
	}

	/**
     * Fetches pending transactions by approval status
     * 
     * @param approvalStatus
     *            The list of transactions 
     * @return list of transactions
     * */
	@Override
	public List<Transaction> getPendingTransactionsByStatus(String approvalStatus) {
		logger.info("Getting pending transactions by approval status");
		if(approvalStatus==null){
			return null;
		}
		return transactionDao.findByApprovalStatus(approvalStatus);
	}


}
