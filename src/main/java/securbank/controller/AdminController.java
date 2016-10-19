/**
 * 
 */
package securbank.controller;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import securbank.dao.UserDao;
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
	private UserDao userDao;

	@Autowired
	private NewUserRequestFormValidator newUserRequestFormValidator;

	final static Logger logger = LoggerFactory.getLogger(AdminController.class);

	@GetMapping("/admin/details")
	public String currentUserDetails(Model model) {
		
		User user = userService.getCurrentUser();
		
		if (user == null) {
			logger.info("GET request: Unauthorized request for admin user detail");
			return "redirect:/error?code=user.notfound";
		}

		logger.info("GET request: Admin user detail");
		model.addAttribute("user", user);

		return "admin/detail";
	}

	@GetMapping("/admin/user/add")
	public String signupForm(Model model, @RequestParam(required = false) Boolean success) {
		if (success != null) {
			model.addAttribute("success", success);
		}
		model.addAttribute("newUserRequest", new NewUserRequest());
		logger.info("GET request: Admin new user request");

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
		}
		;

		logger.info("POST request: Admin new user request");

		return "redirect:/admin/user/add?success=true";
	}

	@GetMapping("/admin/user")
	public String getUsers(Model model) {
		List<User> users = userService.getUsersByType("internal");
		if (users == null) {
			return "redirect:/error?code=500";
		}
		model.addAttribute("users", users);
		logger.info("GET request: All internal users");

		return "admin/internalusers";
	}

	@GetMapping("/admin/user/{id}")
	public String getUserDetail(Model model, @PathVariable UUID id) {
		User user = userService.getUserByIdAndActive(id);
		if (user == null) {
			return "redirect:/error?code=400";
		}
		if (user.getType().equals("external")) {
			logger.warn("GET request: Unauthorized request for external user");

			return "redirect:/error?code=409";
		}

		model.addAttribute("user", user);
		logger.info("GET request: Internal user details by id");

		return "admin/userdetail";
	}

	@RequestMapping("/admin/syslogs")
	public String adminControllerSystemLogs(Model model) {
		return "admin/systemlogs";
	}

}
