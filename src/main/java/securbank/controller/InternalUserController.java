/**
 * 
 */
package securbank.controller;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import securbank.models.ModificationRequest;
import securbank.models.NewUserRequest;
import securbank.models.User;
import securbank.services.UserService;
import securbank.validators.NewUserFormValidator;

/**
 * @author Ayush Gupta
 *
 */
@Controller
public class InternalUserController {
	@Autowired
	private UserService userService;
	
	@Autowired 
	NewUserFormValidator userFormValidator;
	
	@Autowired
    public HttpSession session;
	
	@GetMapping("/internal/user/verify")
    public String verifyNewUser(Model model, @RequestParam UUID id) {
		if (id == null) {
			return "redirect/error";
		}
		NewUserRequest newUserRequest = userService.getNewUserRequest(id);
		if (newUserRequest == null) {
			return "redirect/error?code=400&path=no-request";
		}
		session.setAttribute("verification.token", newUserRequest.getNewUserRequestId());
		User user = new User();
		user.setEmail(newUserRequest.getEmail());
		user.setRole(newUserRequest.getRole());
		model.addAttribute("user", user);
		
		return "internal/signup";
    }
	
	// TODO: add check to verify if this request comes from verification link
	@PostMapping("/internal/user/signup")
    public String internalSignupSubmit(@ModelAttribute User user, BindingResult bindingResult) {
		UUID token = (UUID) session.getAttribute("verification.token");
		if (token == null) {
			return "redirect:/error?code=400&path=bad-request";
		}
		NewUserRequest newUserRequest = userService.getNewUserRequest(token);
		if (newUserRequest == null) {
			return "redirect:/error?code=400&path=token-invalid";
		}
		if(!newUserRequest.getEmail().equals(user.getEmail()) || !newUserRequest.getRole().equals(user.getRole())) {
			return "redirect:/error?code=400";
		}
		userFormValidator.validate(user, bindingResult);
		
		if (bindingResult.hasErrors()) {
			return "internal/signup";
        }
		
		if (userService.createInternalUser(user) == null) {
			return "redirect:/error?code=400&path=user-invalid";
		};
    	
        return "redirect:/login";
    }
}
