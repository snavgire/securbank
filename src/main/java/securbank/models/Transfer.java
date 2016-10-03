package securbank.models;

import java.util.HashSet;
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
import javax.persistence.OneToMany;
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
@Table(name = "Transfer")

public class Transfer {
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@NotNull
	@Column(name = "transferId", unique = true, nullable = false, columnDefinition = "BINARY(16)")
	private UUID transferId;
	
	@NotNull
	@Column(name = "status", unique = false, nullable = false)
	private String status;
	
	@NotNull
	@Column(name = "createdOn", nullable = false, updatable = false)
	private LocalDateTime createdOn;
	
	@Column(name = "modifiedOn", nullable = true, updatable = true)
	private LocalDateTime modifiedOn;

	@NotNull
	@Column(name = "active", nullable = false, columnDefinition = "BIT")
	private Boolean active;
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fromAccountNumber", referencedColumnName = "accountNumber", nullable = false)
	private Account fromAccount;
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "toAccountNumber", referencedColumnName = "accountNumber", nullable = false)
	private Account toAccount;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "transfer")
	private Set<Transaction> transactions = new HashSet<Transaction>(0);

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "userId", nullable = false)
	private User modifiedBy;

	/**
	 * @param transferId
	 * @param status
	 * @param createdOn
	 * @param modifiedOn
	 * @param active
	 * @param fromAccount
	 * @param toAccount
	 * @param transactions
	 * @param modifiedBy
	 */
	public Transfer(UUID transferId, String status, LocalDateTime createdOn, LocalDateTime modifiedOn, Boolean active,
			Account fromAccount, Account toAccount, Set<Transaction> transactions, User modifiedBy) {
		super();
		this.transferId = transferId;
		this.status = status;
		this.createdOn = createdOn;
		this.modifiedOn = modifiedOn;
		this.active = active;
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
		this.transactions = transactions;
		this.modifiedBy = modifiedBy;
	}
	
	public Transfer() {
		
	}

	/**
	 * @return the transferId
	 */
	public UUID getTransferId() {
		return transferId;
	}

	/**
	 * @param transferId the transferId to set
	 */
	public void setTransferId(UUID transferId) {
		this.transferId = transferId;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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

	/**
	 * @return the fromAccount
	 */
	public Account getFromAccount() {
		return fromAccount;
	}

	/**
	 * @param fromAccount the fromAccount to set
	 */
	public void setFromAccount(Account fromAccount) {
		this.fromAccount = fromAccount;
	}

	/**
	 * @return the toAccount
	 */
	public Account getToAccount() {
		return toAccount;
	}

	/**
	 * @param toAccount the toAccount to set
	 */
	public void setToAccount(Account toAccount) {
		this.toAccount = toAccount;
	}

	/**
	 * @return the transactions
	 */
	public Set<Transaction> getTransactions() {
		return transactions;
	}

	/**
	 * @param transactions the transactions to set
	 */
	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Transfer [transferId=" + transferId + ", status=" + status + ", createdOn=" + createdOn
				+ ", modifiedOn=" + modifiedOn + ", active=" + active + ", fromAccount=" + fromAccount + ", toAccount="
				+ toAccount + ", transactions=" + transactions + ", modifiedBy=" + modifiedBy + "]";
	}
}
