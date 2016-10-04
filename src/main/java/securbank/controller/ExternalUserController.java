/**
 * 
 */
package securbank.controller;

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
	
	@GetMapping("/user/edit")
    public String editUser(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error";
		}
		model.addAttribute("user", user);
		
        return "signup";
    }
	
	@PostMapping("/user/edit")
    public String editSubmit(@ModelAttribute ModificationRequest request, BindingResult bindingResult) {
		editUserFormValidator.validate(request, bindingResult);
		if (bindingResult.hasErrors()) {
			return "edit";
        }
		
		// create request
    	userService.createExternalModificationRequest(request);
	
        return "redirect:/";
    }
	
	@GetMapping("/user/details")
    public String currentUserDetails(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error?code=400&path=user-notfound";
		}
		
		model.addAttribute("user", user);
		
        return "external/detail";
    }
}
