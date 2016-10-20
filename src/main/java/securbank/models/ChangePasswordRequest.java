package securbank.models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

/**
 * @author Shivani Jhunjhunwala
 *
 */
@Entity
@Component("changePasswordRequest")
public class ChangePasswordRequest {
	
	@Id
	@NotNull
	private String existingPassword;
	
	@NotNull
	private String newPassword;
	
	@NotNull
	private String confirmPassword;
	
	public ChangePasswordRequest(){
		
	}
	/**
	 * @param userId
	 * @param existingPassword
	 * @param newPassword
	 * @param confirmPassword

	 */
	public ChangePasswordRequest(String existingPassword, String newPassword, String confirmPassword){
		super();
		this.existingPassword = existingPassword;
		this.newPassword = newPassword;
		this.confirmPassword = confirmPassword;
	}
	
	
	/**
	 * @return the password
	 */
	public String getExistingPassword() {
		return existingPassword;
	}
	
	/**
	 * @param password the password to set
	 */
	public void setExistingPassword(String existingPassword) {
		this.existingPassword = existingPassword;
	}
	

	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	/**
	 * @return the confirmPassword
	 */
	public String getConfirmPassword() {
		return confirmPassword;
	}
	
	/**
	 * @param confirmPassword the confirmPassword to set
	 */
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
