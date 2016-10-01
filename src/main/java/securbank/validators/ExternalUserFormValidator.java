package securbank.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.ValidationUtils;

import securbank.models.User;
import securbank.Utils;
/**
 * @author Ayush Gupta
 *
 */
@Component("externameFserFormValidator")
public class ExternalUserFormValidator implements Validator{

	Utils utils = new Utils();
	
	/**
     * If supports class
     * 
     * @param clazz
     *            The class to check
     *            
     * @return boolean
     */	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	/**
     * Validates create user form
     * 
     * @param target
     *            The target object
     * @param errors
     *            The errors object
     */
	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "user.username.required", "Username is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "user.firstName.required", "First Name is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "user.lastName.required", "Last Name is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "user.password.required", "Password is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "user.phone.required", "Phone Number is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "addressLine1", "user.addressLine1.required", "Address is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "city", "user.city.required", "City is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "zip", "user.zip.required", "Zip is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "state", "user.phone.required", "State is required");
		
		if (!errors.hasFieldErrors("email") && !utils.validateEmail(user.getEmail())) {
					errors.rejectValue("email", "user.email.contraint", "Invalid Email");
		}
		
		if (!errors.hasFieldErrors("username") && !utils.validateUsername(user.getUsername())) {
			errors.rejectValue("username", "user.username.contraint", "Username can contain lowercase alphanumeric with (-,_) and length should be between 3 and 15");
		}
		
		if (!errors.hasFieldErrors("password") && !utils.validatePassword(user.getPassword())) {
			errors.rejectValue("password", "user.password.contraint", "Password should contain one letter, number and special character. Length of password should be between 6 and 20");
		}
		
		if (!errors.hasFieldErrors("phone") && !utils.validatePhone(user.getPhone())) {
			errors.rejectValue("Phone", "user.phone.contraint", "Invalid Phone");
		}
		
		if (!errors.hasFieldErrors("zip") && !utils.validateZip(user.getZip())) {
			errors.rejectValue("zip", "user.zip.invalid", "Invalid Zip");
		}
	} 
}
