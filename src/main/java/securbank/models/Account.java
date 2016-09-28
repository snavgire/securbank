package securbank.models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import org.joda.time.LocalDateTime;

/**
 * 
 * 
 * @author Madhu Illuri
 *
 */


@Entity 
@Table(name = "Account")
public class Account {
	
	
	/** map ManyToOne to many side of the relationship */
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "userId", nullable = false)
	private User user;
	
	/** Account number is unique. */
	@Id
	@NotNull
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Size(min = 5, max = 15)
	@Column(name = "accountNumber", nullable = false, length = 10, unique = true, columnDefinition = "BINARY(16)")
	private UUID accountNumber;
	
	/** UserID is unique	 */
	@NotNull
	@Column(name = "userId", unique = true, nullable = false, columnDefinition = "BINARY(16)")
	private UUID userId;
	
	
	/** balance on the account */
	@NotNull
	@Column(name = "balance", unique = false, nullable = false)
	private Double balance;
	
	
	/** account type. Default an account will be checkings(0), (1) for savings account	*/
	@NotNull
	@Column(name = "accountType", unique = false, nullable = false, columnDefinition = "int(1) DEFAULT '0' ")
	private Integer accountType;
	
	@NotNull
	@Column(name = "createdOn", nullable = false, updatable = false)
	private LocalDateTime createdOn;
	
	@NotNull
	@Column(name = "active", nullable = false, columnDefinition = "BIT")
	private Boolean active;
	
	
	
	
	
	/**
	 * 
	 * @param accountNumber
	 * @param userId
	 * @param balance
	 * @param accountType
	 * @param createdOn
	 * @param active
	 */
	public Account(UUID accountNumber, UUID userId, double balance, int accountType, LocalDateTime createdOn
			,Boolean active){
		
		super();
		this.accountNumber = accountNumber;
		this.userId = userId;
		this.balance = balance;
		this.accountType = accountType;
		this.createdOn = createdOn;
		this.active = active;
		
		
	}
	
	/**
	 * 
	 * @return accountNumber
	 */
	public UUID getAccountNumber() {
		return accountNumber;
	}
	
	/**
	 * 
	 * @param accountNumber sets accountNumber
	 */
	public void setAccountNumber(UUID accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	/**
	 * 
	 * @return userId
	 */
	public UUID getUserId() {
		return userId;
	}
	
	/**
	 * 
	 * @param userId sets userId
	 */
	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	/**
	 * 
	 * @return balance
	 */
	public Double getBalance() {
		return balance;
	}
	
	/**
	 * 
	 * @param balance sets balance
	 */
	public void setBalance(Double balance) {
		this.balance = balance;
	}

	/**
	 * 
	 * @return accountType
	 */
	public Integer getAccountType() {
		return accountType;
	}
	
	/**
	 * 
	 * @param accountType sets accountType
	 */
	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}
	
	
	/**
	 * 
	 * @return createdOn time created on
	 */
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}
	
	/**
	 * 
	 * @param createdOn sets time created on 
	 */
	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}
	
	
	/**
	 * 
	 * @return active if the account is active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * 
	 * @param active sets account active
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}



	
	
	

}
