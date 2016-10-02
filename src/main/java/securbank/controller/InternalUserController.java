/**
 * 
 */
package securbank.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import securbank.models.NewUserRequest;
import securbank.models.User;
import securbank.services.UserService;
import securbank.validators.NewUserFormValidator;
import securbank.validators.NewUserRequestFormValidator;

/**
 * @author Ayush Gupta
 *
 */
@Controller
public class InternalUserController {
	@Autowired
	private UserService userService;
	
	@Autowired 
	private NewUserRequestFormValidator newUserRequestFormValidator;
	
	@Autowired 
	NewUserFormValidator userFormValidator;
	
	@GetMapping("/admin/register")
    public String signupForm(Model model) {
		model.addAttribute("newUserRequest", new NewUserRequest());

		return "newuserrequest";
    }

	@GetMapping("/admin/register/success")
    public String signupSuccess(Model model) {
		return "home";
    }

	@PostMapping("/admin/register")
    public String signupSubmit(@ModelAttribute NewUserRequest newUserRequest, BindingResult bindingResult) {
		newUserRequestFormValidator.validate(newUserRequest, bindingResult);
		if (bindingResult.hasErrors()) {
			return "newuserrequest";
        }
		if (userService.createUserRequest(newUserRequest) == null) {
			return "redirect:/error";
		};
    	
        return "redirect:/admin/register";
    }
	

	@GetMapping("/internal/verify")
    public String verifyNewUser(Model model, @RequestParam UUID id) {
		if (id == null) {
			return "redirect/error";
		}
		NewUserRequest newUserRequest = userService.getNewUserRequest(id);
		if (newUserRequest == null) {
			return "redirect/error";
		}
		User user = new User();
		user.setEmail(newUserRequest.getEmail());
		user.setRole(newUserRequest.getRole());
		model.addAttribute("user", user);
		  
		return "internal_signup";
    }
	
	@PostMapping("/internal/signup")
    public String internalSignupSubmit(@ModelAttribute User user, BindingResult bindingResult) {
		userFormValidator.validate(user, bindingResult);
		
		if (bindingResult.hasErrors()) {
			return "internal_signup";
        }
		
		if (userService.createInternalUser(user) == null) {
			return "redirect:/error";
		};
    	
        return "redirect:/";
    }

	@PostMapping("/internal/edit/approve")
    public String rejectEdit(@ModelAttribute UUID requestId, BindingResult bindingResult) {
		if (requestId == null) {
			return "redirect:/error";
		}
		
		// approves request
		if (userService.approveModificationRequest(requestId) == null) {
    		return "redirect:/error";
    	}
    	
        return "redirect:/";
    }
	
	@PostMapping("/internal/edit/reject")
    public String approveEdit(@ModelAttribute UUID requestId, BindingResult bindingResult) {
		if (requestId == null) {
			return "redirect:/error";
		}
		
		// rejects request
		if (userService.rejectModificationRequest(requestId) == null) {
    		return "redirect:/error";
    	}
    	
        return "redirect:/";
    }

}
