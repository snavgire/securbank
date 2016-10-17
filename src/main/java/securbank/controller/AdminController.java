/**
 * 
 */
package securbank.controller;

import java.util.ArrayList;
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

import securbank.models.ModificationRequest;
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
		if (userService.createNewUserRequest(newUserRequest) == null) {
			return "redirect:/error?code=500";
		}

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
			return "redirect:/error?code=404";
		}
		if (user.getType().equals("external")) {
			logger.warn("GET request: Unauthorized request for external user");

			return "redirect:/error?code=409";
		}

		model.addAttribute("user", user);
		logger.info("GET request: Internal user details by id");
        	
        return "admin/userdetail";
    }
	
	@GetMapping("/admin/user/request")
    public String getAllUserRequest(Model model) {
		List<ModificationRequest> modificationRequests = userService.getModificationRequests("pending", "internal");
		if (modificationRequests == null) {
			model.addAttribute("modificationrequests", new ArrayList<ModificationRequest>());
		}
		else {
			model.addAttribute("modificationrequests", modificationRequests);	
		}
		logger.info("GET request: All user requests");
		
        return "admin/modificationrequests";
    }
	
	@GetMapping("/admin/user/request/view/{id}")
    public String getUserRequest(Model model, @PathVariable() UUID id) {
		ModificationRequest modificationRequest = userService.getModificationRequest(id);
		
		if (modificationRequest == null) {
			return "redirect:/error?code=404&path=request-invalid";
		}
		model.addAttribute("modificationrequest", modificationRequest);
		logger.info("GET request: User modification request by ID");
		
		
        return "admin/modificationrequest_detail";
    }
	
	@PostMapping("/admin/user/request/{requestId}")
    public String approveEdit(@PathVariable UUID requestId, @ModelAttribute ModificationRequest request, BindingResult bindingResult) {
		String status = request.getStatus();
		if (status == null || !(request.getStatus().equals("approved") || request.getStatus().equals("rejected"))) {
			return "redirect:/error?code=400&path=request-action-invalid";
		}
		request = userService.getModificationRequest(requestId);
		
		// checks validity of request
		if (request == null) {
			return "redirect:/error?code=404&path=request-invalid";
		}
		
		// checks if admin is authorized for the request to approve
		if (!request.getUserType().equals("internal")) {
			logger.warn("GET request: Admin unauthrorised request access");
			
			return "redirect:/error?code=401&path=request-unauthorised";
		}
		request.setStatus(status);
		if (status.equals("approved")) {
			userService.approveModificationRequest(request);
		}
		// rejects request
		else {
			userService.rejectModificationRequest(request);
		}
		logger.info("POST request: Admin approves modification request");
		
        return "redirect:/admin/user/request";
    }	

	@GetMapping("/admin/user/request/delete/{id}")
    public String getDeleteRequest(Model model, @PathVariable() UUID id) {
		ModificationRequest modificationRequest = userService.getModificationRequest(id);
		
		if (modificationRequest == null) {
			return "redirect:/error?code=404&path=request-invalid";
		}
		model.addAttribute("modificationrequest", modificationRequest);
		logger.info("GET request: User modification request by ID");
		
		
        return "admin/modificationrequest_delete";
    }
	
	@PostMapping("/admin/user/request/delete/{requestId}")
    public String deleteRequest(@PathVariable UUID requestId, @ModelAttribute ModificationRequest request, BindingResult bindingResult) {
		request = userService.getModificationRequest(requestId);
		
		// checks validity of request
		if (request == null) {
			return "redirect:/error?code=404&path=request-invalid";
		}
		
		// checks if admin is authorized for the request to approve
		if (!request.getUserType().equals("internal")) {
			logger.warn("GET request: Admin unauthrorised request access");
			
			return "redirect:/error?code=401&path=request-unauthorised";
		}
		userService.deleteModificationRequest(request);
		logger.info("POST request: Admin approves modification request");
		
        return "redirect:/admin/user/request";
    }	
	
	@RequestMapping("/admin/syslogs")
	public String adminControllerSystemLogs(Model model) {
		return "admin/systemlogs";
	}

}
