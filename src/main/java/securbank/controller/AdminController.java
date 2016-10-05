/**
 * 
 */
package securbank.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import securbank.models.NewUserRequest;
import securbank.models.User;
import securbank.services.UserService;
import securbank.validators.NewUserRequestFormValidator;

/**
 * @author Ayush Gupta
 *
 */
@Controller
public class AdminController {
	@Autowired
	private UserService userService;
	
	@Autowired 
	private NewUserRequestFormValidator newUserRequestFormValidator;
	

	@GetMapping("/admin/details")
    public String currentUserDetails(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error?code=user.notfound";
		}
		
		model.addAttribute("user", user);
		
        return "admin/detail";
    }
	
	@GetMapping("/admin/user/add")
    public String signupForm(Model model, @RequestParam(required = false) Boolean success) {
		if (success != null) {
			model.addAttribute("success", success);
		}
		model.addAttribute("newUserRequest", new NewUserRequest());

		return "admin/newuserrequest";
    }

	@PostMapping("/admin/user/add")
    public String signupSubmit(@ModelAttribute NewUserRequest newUserRequest, BindingResult bindingResult) {
		newUserRequestFormValidator.validate(newUserRequest, bindingResult);
		if (bindingResult.hasErrors()) {
			return "admin/newuserrequest";
        }
		if (userService.createUserRequest(newUserRequest) == null) {
			return "redirect:/error";
		};
    	
        return "redirect:/admin/user/add?success=true";
    }
	
	@GetMapping("/admin/user")
    public String getUsers(Model model) {
		List<User> users = userService.getUsersByType("internal");
		if (users == null) {
			return "redirect:/error?code=500";
		}
		model.addAttribute("users", users);
			
        return "admin/internalusers";
    }
	
	@GetMapping("/admin/user/{id}")
    public String getUserDetail(Model model, @PathVariable UUID id) {
		User user = userService.getUserByIdAndActive(id);
		if (user == null) {
			return "redirect:/error?code=400";
		}
		if (user.getType().equals("external")) {
			return "redirect:/error?code=409";
		}
		
		model.addAttribute("user", user);
			
        return "admin/userdetail";
    }

}
