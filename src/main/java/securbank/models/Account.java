package securbank.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.joda.time.LocalDateTime;

/**
 * 
 * @author Madhu Illuri
 */

@Entity 
@Table(name = "Account")
public class Account {
	
	/** Account number is unique. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long accountNumber;
	
	/** balance on the account */
	@NotNull
	private Double balance;
	
	@NotNull
	private String type;
	
	@NotNull
	@Column(name = "createdOn", updatable = false)
	private LocalDateTime createdOn;
	
	@NotNull
	@Column(name = "active", columnDefinition = "BIT")
	private Boolean active;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "userId", nullable = false)
	private User user;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "account")
	private CreditCard creditCard;

	/**
	 * @param accountNumber
	 * @param balance
	 * @param type
	 * @param createdOn
	 * @param active
	 * @param user
	 * @param creditCard
	 */
	public Account(Long accountNumber, Double balance, String type, LocalDateTime createdOn, Boolean active, User user,
			CreditCard creditCard) {
		super();
		this.accountNumber = accountNumber;
		this.balance = balance;
		this.type = type;
		this.createdOn = createdOn;
		this.active = active;
		this.user = user;
		this.creditCard = creditCard;
	}
	
	public Account() {
		
	}
	
	/**
	 * @return the accountNumber
	 */
	public Long getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the balance
	 */
	public Double getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(Double balance) {
		this.balance = balance;
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
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the creditCard
	 */
	public CreditCard getCreditCard() {
		return creditCard;
	}

	/**
	 * @param creditCard the creditCard to set
	 */
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Account [accountNumber=" + accountNumber + ", balance=" + balance + ", type=" + type + ", createdOn="
				+ createdOn + ", active=" + active + ", user=" + user + ", creditCard=" + creditCard + "]";
	}
}
