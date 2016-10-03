package securbank.models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Email;
import org.joda.time.LocalDateTime;

/**
 * @author Ayush Gupta
 *
 */
@Entity
@Table(name = "modificationRequest")
public class ModificationRequest {
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "modificationRequestId", unique = true, columnDefinition = "BINARY(16)")
	private UUID modificationRequestId;
	
	@NotNull
	private String role;
	
	@NotNull
	@Transient
	private String username;
	
	@NotNull
	@Size(min = 60, max = 60)
	private String password;
	
	@NotNull
	@Size(min = 2)
	private String firstName;
	
	@Size(min = 0)
	private String middleName;
	
	@NotNull
	@Size(min = 2)
	private String lastName;
	
	@NotNull
	@Email
	@Column(name = "email")
	private String email;

	@NotNull
	@Size(min = 10, max = 10)
	@Column(name = "phone")
	private String phone;

	@NotNull
	@Size(min = 2, max = 50)
	private String addressLine1;

	@Size(min = 0, max = 50)
	private String addressLine2;

	@NotNull
	@Size(min = 2, max = 35)
	private String city;

	@NotNull
	@Size(min = 2, max = 35)
	private String State;

	@NotNull
	@Size(min = 5, max = 5)
	private String zip;

	@NotNull
	@Column(name = "createdOn", updatable = false)
	private LocalDateTime createdOn;

	@Column(name = "modifiedOn", updatable = true)
	private LocalDateTime modifiedOn;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "approvedByUserId", referencedColumnName = "userId")
	private User approvedBy;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userId", referencedColumnName = "userId")
	private User user;	
	
	@NotNull
	@Column(name = "status")
	private String status;
	
	@NotNull
	@Column(name = "active", columnDefinition = "BIT")
	private Boolean active;
	
	public ModificationRequest() {
		
	}

	/**
	 * @param modificationRequestId
	 * @param role
	 * @param username
	 * @param password
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @param email
	 * @param phone
	 * @param addressLine1
	 * @param addressLine2
	 * @param city
	 * @param State
	 * @param zip
	 * @param createdOn
	 * @param modifiedOn
	 * @param approvedBy
	 * @param user
	 * @param status
	 * @param active
	 */
	public ModificationRequest(UUID modificationRequestId, String role, String username, String password,
			String firstName, String middleName, String lastName, String email, String phone, String addressLine1,
			String addressLine2, String city, String State, String zip, LocalDateTime createdOn,
			LocalDateTime modifiedOn, User approvedBy, User user, String status, Boolean active) {
		super();
		this.modificationRequestId = modificationRequestId;
		this.role = role;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.city = city;
		this.State = State;
		this.zip = zip;
		this.createdOn = createdOn;
		this.modifiedOn = modifiedOn;
		this.approvedBy = approvedBy;
		this.user = user;
		this.status = status;
		this.active = active;
	}

	/**
	 * @return the modificationstatusId
	 */
	public UUID getModificationRequestId() {
		return modificationRequestId;
	}

	/**
	 * @param modificationstatusId the modificationstatusId to set
	 */
	public void setModificationRequestId(UUID modificationRequestId) {
		this.modificationRequestId = modificationRequestId;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the addressLine1
	 */
	public String getAddressLine1() {
		return addressLine1;
	}

	/**
	 * @param addressLine1 the addressLine1 to set
	 */
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	/**
	 * @return the addressLine2
	 */
	public String getAddressLine2() {
		return addressLine2;
	}

	/**
	 * @param addressLine2 the addressLine2 to set
	 */
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the State
	 */
	public String getState() {
		return State;
	}

	/**
	 * @param State the State to set
	 */
	public void setState(String State) {
		this.State = State;
	}

	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
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
	 * @return the approvedBy
	 */
	public User getApprovedBy() {
		return approvedBy;
	}

	/**
	 * @param approvedBy the approvedBy to set
	 */
	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
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
		return "Modificationstatus [modificationRequestId=" + modificationRequestId + ", role=" + role + ", username="
				+ username + ", password=" + password + ", firstName=" + firstName + ", middleName=" + middleName
				+ ", lastName=" + lastName + ", email=" + email + ", phone=" + phone + ", addressLine1=" + addressLine1
				+ ", addressLine2=" + addressLine2 + ", city=" + city + ", State=" + State + ", zip=" + zip
				+ ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + ", approvedBy=" + approvedBy + ", user="
				+ user + ", status=" + status + ", active=" + active + "]";
	}
	
	
}
