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

import securbank.models.ModificationRequest;
import securbank.models.User;
import securbank.services.UserService;
import securbank.validators.EditUserFormValidator;
import securbank.validators.NewUserFormValidator;

/**
 * @author Ayush Gupta
 *
 */
@Controller
public class ExternalUserController {
	@Autowired
	UserService userService;
	
	@Autowired 
	NewUserFormValidator userFormValidator;
	
	@Autowired 
	EditUserFormValidator editUserFormValidator;
	
	@GetMapping("/signup")
    public String signupForm(Model model) {
		model.addAttribute("user", new User());

		return "signup";
    }
		
	@PostMapping("/signup")
    public String signupSubmit(@ModelAttribute User user, BindingResult bindingResult) {
		userFormValidator.validate(user, bindingResult);
		if (bindingResult.hasErrors()) {
			return "signup";
        }
		
    	userService.createExternalUser(user);
    	
        return "redirect:/";
    }
	
	@GetMapping("/user/edit")
    public String currentUserEdit(Model model) {
		model.addAttribute(userService.getCurrentUser());
		
        return "edit";
    }
	
	@PostMapping("/user/edit/")
    public String editSubmit(@ModelAttribute ModificationRequest request, BindingResult bindingResult) {
		editUserFormValidator.validate(request, bindingResult);
		if (bindingResult.hasErrors()) {
			return "edit";
        }
		
		// create request
    	userService.createModificationRequest(request);
    	
        return "redirect:/";
    }
	
	@GetMapping("/user/details")
    public String currentUserDetails(Model model) {
		model.addAttribute(userService.getCurrentUser());
		
        return "details";
    }	
}
