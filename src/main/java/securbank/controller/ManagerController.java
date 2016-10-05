/**
 * 
 */
package securbank.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import securbank.models.User;
import securbank.services.UserService;

/**
 * @author Ayush Gupta
 *
 */
@Controller
public class ManagerController {
	@Autowired
	private UserService userService;
	
	@GetMapping("/manager/details")
    public String currentUserDetails(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error?code=400&path=user.notfound";
		}
		
		model.addAttribute("user", user);
			
        return "manager/detail";
    }
	
	@GetMapping("/manager/user")
    public String getUsers(Model model) {
		List<User> users = userService.getUsersByType("external");
		if (users == null) {
			return "redirect:/error?code=500";
		}
		model.addAttribute("users", users);
			
        return "manager/externalusers";
    }
	
	@GetMapping("/manager/user/{id}")
    public String getUserDetails(Model model, @PathVariable UUID id) {
		User user = userService.getUserByIdAndActive(id);
		if (user == null) {
			return "redirect:/error?code=400";
		}
		if (user.getType().equals("internal")) {
			return "redirect:/error?code=409";
		}
		
		model.addAttribute("user", user);
			
        return "manager/userdetail";
    }
}
