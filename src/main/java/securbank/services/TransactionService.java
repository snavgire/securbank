package securbank.services;

import java.util.List;
import securbank.models.Transaction;
import securbank.models.Transfer;

/**
 * @author Mitikaa
 *
 */
public interface TransactionService {
	public Transaction initiateDebit(Transaction transaction);
	public Transaction initiateCredit(Transaction transaction);
	public Transaction initiateTransfer(Transfer transfer);
	public Transaction approveTransaction(Transaction transaction);
	public Transaction approveTransfer(Transfer transfer);
	public Transaction declineTransaction(Transaction transaction);
	public Transaction declineTransaction(Transfer transfer);
	public List<Transaction> getPendingTransactionsByAccountNumber(String accountNumber);
	public List<Transaction> getPendingTransactionsByType(String accountNumber, String accountType);
	public List<Transaction> getPendingTransactionsByStatus(String approvalStatus);
}
