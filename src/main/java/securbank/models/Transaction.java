package securbank.models;

import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.joda.time.LocalDateTime;

/**
 * @author Mitikaa Sama
 *
 * Sep 26, 2016
 */

@Entity
@Table(name = "Transaction")

public class Transaction {
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@NotNull
	@Column(name = "transactionId", unique = true, nullable = false, columnDefinition = "BINARY(16)")
	private UUID transactionId;
	
	@NotNull
	@Size(min = 8, max = 8)
	@Column(name = "accountNumber", unique = false, nullable = false, updatable = false)
	private String accountNumber;
	
	@NotNull
	@Column(name = "amount", unique = false, nullable = false, updatable = false)
	private double amount;
	
	@NotNull
	@Column(name = "type", unique = false, nullable = false)
	private String type;
	
	@NotNull
	@Column(name = "oldBalance", unique = false, nullable = false, updatable = false)
	private double oldBalance;
	
	@NotNull
	@Column(name = "newBalance", unique = false, nullable = false, updatable = false)
	private double newBalance;
	
	@NotNull
	@Column(name = "criticalStatus", unique = false, nullable = false, columnDefinition = "BIT")
	private boolean criticalStatus;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "transferId", nullable = false)
	private Transfer transfer;
	
	@NotNull
	@Column(name = "createdOn", nullable = false, updatable = false)
	private LocalDateTime createdOn;
	
	@Column(name = "modifiedOn", nullable = true, updatable = true)
	private LocalDateTime modifiedOn;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Column(name = "userId", nullable = true, updatable = true)
	private Set<User> modifiedBy;

	@NotNull
	@Column(name = "active", nullable = false, columnDefinition = "BIT")
	private Boolean active;
	
	/**
	 * @param transactionId
	 * @param accountNumber
	 * @param amount
	 * @param type
	 * @param oldBalance
	 * @param newBalance
	 * @param criticalStatus
	 * @param transferId
	 * @param createdOn
	 * @param modifiedOn
	 * @param modifiedBy
	 * @param active
	 */
	public Transaction(UUID transactionId, String accountNumber, double amount, 
			String type, double oldBalance, double newBalance, Boolean criticalStatus, 
			Transfer transfer, LocalDateTime createdOn, LocalDateTime modifiedOn, Set<User> modifiedBy, Boolean active){
		super();
		this.transactionId = transactionId;
		this.accountNumber = accountNumber;
		this.amount = amount;
		this.type = type;
		this.oldBalance = oldBalance;
		this.newBalance = newBalance;
		this.criticalStatus = criticalStatus;
		this.transfer = transfer;
		this.createdOn = createdOn;
		this.modifiedOn = modifiedOn;
		this.modifiedBy = modifiedBy;
		this.active = active;
		
	}

	
	/**
	 * @return the transfer
	 */
	public Transfer getTransfer() {
		return transfer;
	}


	/**
	 * @return the modifiedOn
	 */
	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}


	/**
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}


	/**
	 * @param transfer the transfer to set
	 */
	public void setTransfer(Transfer transfer) {
		this.transfer = transfer;
	}


	/**
	 * @param modifiedOn the modifiedOn to set
	 */
	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}


	/**
	 * @return the modifiedBy
	 */
	public Set<User> getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(Set<User> modifiedBy) {
		this.modifiedBy = modifiedBy;
	}


	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}


	/**
	 * @return the transactionId
	 */
	public UUID getTransactionId() {
		return transactionId;
	}


	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}


	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}


	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}


	/**
	 * @return the oldBalance
	 */
	public double getOldBalance() {
		return oldBalance;
	}

	
	/**
	 * @return the newBalance
	 */
	public double getNewBalance() {
		return newBalance;
	}


	/**
	 * @return the criticalStatus
	 */
	public boolean isCriticalStatus() {
		return criticalStatus;
	}


	/**
	 * @return the createdOn
	 */
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}


	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(UUID transactionId) {
		this.transactionId = transactionId;
	}


	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}


	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}


	/**
	 * @param oldBalance the oldBalance to set
	 */
	public void setOldBalance(double oldBalance) {
		this.oldBalance = oldBalance;
	}


	/**
	 * @param newBalance the newBalance to set
	 */
	public void setNewBalance(double newBalance) {
		this.newBalance = newBalance;
	}


	/**
	 * @param criticalStatus the criticalStatus to set
	 */
	public void setCriticalStatus(boolean criticalStatus) {
		this.criticalStatus = criticalStatus;
	}


//	/**
//	 * @param transferId the transferId to set
//	 */
//	public void setTransferId(UUID transferId) {
//		this.transferId = transferId;
//	}


	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", accountNumber=" + accountNumber + ", amount=" + amount
				+ ", type=" + type + ", oldBalance=" + oldBalance + ", newBalance=" + newBalance + ", criticalStatus="
				+ criticalStatus + ", transfer=" + transfer + ", createdOn=" + createdOn + "]";
	}


}
