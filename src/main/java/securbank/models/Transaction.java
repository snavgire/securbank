package securbank.models;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
	
	@NotNull
	@Column(name = "createdOn", nullable = false, updatable = false)
	private LocalDateTime createdOn;
	
	@Column(name = "modifiedOn", nullable = true, updatable = true)
	private LocalDateTime modifiedOn;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accountNumber", nullable = false)
	private Account account;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "transferId", nullable = true)
	private Transfer transfer;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "userId", nullable = true)
	private User modifiedBy;

	@NotNull
	@Column(name = "active", nullable = false, columnDefinition = "BIT")
	private Boolean active;

	/**
	 * @param transactionId
	 * @param amount
	 * @param type
	 * @param oldBalance
	 * @param newBalance
	 * @param criticalStatus
	 * @param createdOn
	 * @param modifiedOn
	 * @param account
	 * @param transfer
	 * @param modifiedBy
	 * @param active
	 */
	public Transaction(UUID transactionId, double amount, String type, double oldBalance, double newBalance,
			boolean criticalStatus, LocalDateTime createdOn, LocalDateTime modifiedOn, Account account,
			Transfer transfer, User modifiedBy, Boolean active) {
		super();
		this.transactionId = transactionId;
		this.amount = amount;
		this.type = type;
		this.oldBalance = oldBalance;
		this.newBalance = newBalance;
		this.criticalStatus = criticalStatus;
		this.createdOn = createdOn;
		this.modifiedOn = modifiedOn;
		this.account = account;
		this.transfer = transfer;
		this.modifiedBy = modifiedBy;
		this.active = active;
	}
	
	public Transaction() {
		
	}

	/**
	 * @return the transactionId
	 */
	public UUID getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(UUID transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the oldBalance
	 */
	public double getOldBalance() {
		return oldBalance;
	}

	/**
	 * @param oldBalance the oldBalance to set
	 */
	public void setOldBalance(double oldBalance) {
		this.oldBalance = oldBalance;
	}

	/**
	 * @return the newBalance
	 */
	public double getNewBalance() {
		return newBalance;
	}

	/**
	 * @param newBalance the newBalance to set
	 */
	public void setNewBalance(double newBalance) {
		this.newBalance = newBalance;
	}

	/**
	 * @return the criticalStatus
	 */
	public boolean isCriticalStatus() {
		return criticalStatus;
	}

	/**
	 * @param criticalStatus the criticalStatus to set
	 */
	public void setCriticalStatus(boolean criticalStatus) {
		this.criticalStatus = criticalStatus;
	}

	/**
	 * @return the createdOn
	 */
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the modifiedOn
	 */
	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}

	/**
	 * @param modifiedOn the modifiedOn to set
	 */
	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * @return the transfer
	 */
	public Transfer getTransfer() {
		return transfer;
	}

	/**
	 * @param transfer the transfer to set
	 */
	public void setTransfer(Transfer transfer) {
		this.transfer = transfer;
	}

	/**
	 * @return the modifiedBy
	 */
	public User getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", amount=" + amount + ", type=" + type + ", oldBalance="
				+ oldBalance + ", newBalance=" + newBalance + ", criticalStatus=" + criticalStatus + ", createdOn="
				+ createdOn + ", modifiedOn=" + modifiedOn + ", account=" + account + ", transfer=" + transfer
				+ ", modifiedBy=" + modifiedBy + ", active=" + active + "]";
	}
}
