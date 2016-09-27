package securbank.models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
@Table(name = "Transfer")

public class Transfer {
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@NotNull
	@Column(name = "transferId", unique = true, nullable = false, columnDefinition = "BINARY(16)")
	private UUID transferId;
	
	@NotNull
	@Size(min = 8, max = 8)
	@Column(name = "fromAccountNumber", unique = false, nullable = false, updatable = false)
	private String fromAccountNumber;
	
	@NotNull
	@Size(min = 8, max = 8)
	@Column(name = "toAccountNumber", unique = false, nullable = false, updatable = false)
	private String toAccountNumber;
	
	@NotNull
	@Column(name = "status", unique = false, nullable = false)
	private String status;
	
	@NotNull
	@Column(name = "createdOn", nullable = false, updatable = false)
	private LocalDateTime createdOn;
	
	/**
	 * @param transferId
	 * @param fromAccountNumber
	 * @param toAccountNumber
	 * @param status
	 * @param createdOn
	 */
	public Transfer(UUID transferId, String fromAccountNumber, String toAccountNumber,  
			String status, LocalDateTime createdOn){
		this.transferId = transferId;
		this.fromAccountNumber = fromAccountNumber;
		this.toAccountNumber = toAccountNumber;
		this.status = status;
		this.createdOn = createdOn;
	}

	/**
	 * @return the transferId
	 */
	public UUID getTransferId() {
		return transferId;
	}

	/**
	 * @return the fromAccountNumber
	 */
	public String getFromAccountNumber() {
		return fromAccountNumber;
	}

	/**
	 * @return the toAccountNumber
	 */
	public String getToAccountNumber() {
		return toAccountNumber;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return the createdOn
	 */
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param transferId the transferId to set
	 */
	public void setTransferId(UUID transferId) {
		this.transferId = transferId;
	}

	/**
	 * @param fromAccountNumber the fromAccountNumber to set
	 */
	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

	/**
	 * @param toAccountNumber the toAccountNumber to set
	 */
	public void setToAccountNumber(String toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

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
		return "Transfer [transferId=" + transferId + ", fromAccountNumber=" + fromAccountNumber + ", toAccountNumber="
				+ toAccountNumber + ", status=" + status + ", createdOn=" + createdOn + "]";
	}

	
}
