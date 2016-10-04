/**
 * 
 */
package securbank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import securbank.models.ModificationRequest;
import securbank.models.User;
import securbank.services.UserService;
<<<<<<< HEAD
import securbank.validators.EditUserFormValidator;
import securbank.validators.NewUserFormValidator;
=======
>>>>>>> f717bbd5088f1c8fde7984a13c4a0a83ffd14fea

/**
 * @author Ayush Gupta
 *
 */
@Controller
public class ExternalUserController {
	@Autowired
	UserService userService;
	
<<<<<<< HEAD
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
    	userService.createModificationRequest(request);
	
        return "redirect:/";
    }
	
=======
>>>>>>> f717bbd5088f1c8fde7984a13c4a0a83ffd14fea
	@GetMapping("/user/details")
    public String currentUserDetails(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
<<<<<<< HEAD
			return "redirect:/error";
		}
		
		model.addAttribute("user", userService.getCurrentUser());
		
		return "details";
    }	
=======
			return "redirect:/error?code=user-notfound";
		}
		
		model.addAttribute("user", user);
		
        return "internal/detail";
    }
>>>>>>> f717bbd5088f1c8fde7984a13c4a0a83ffd14fea
}
