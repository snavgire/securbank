/**
 * 
 */
package securbank.controller;

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

import securbank.models.User;
import securbank.models.ViewAuthorization;
import securbank.services.UserService;
import securbank.services.ViewAuthorizationService;
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
	ViewAuthorizationService viewAuthorizationService;

	@Autowired 
	NewUserFormValidator userFormValidator;
	
	@Autowired 
	EditUserFormValidator editUserFormValidator;
	
	final static Logger logger = LoggerFactory.getLogger(ExternalUserController.class);

	@GetMapping("/user/details")
    public String currentUserDetails(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error?code=400&path=user-notfound";
		}
		
		model.addAttribute("user", user);
		logger.info("GET request: External user detail");
		
        return "external/detail";
    }
	
	@GetMapping("/user/edit")
    public String editUser(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error";
		}
		model.addAttribute("user", user);
		
        return "external/edit";
    }
	
	@PostMapping("/user/edit")
    public String editSubmit(@ModelAttribute User user, BindingResult bindingResult) {
		editUserFormValidator.validate(user, bindingResult);
		if (bindingResult.hasErrors()) {
			return "external/edit";
        }
		
		// create request
    	userService.createExternalModificationRequest(user);
	
        return "redirect:/";
    }
	
	@GetMapping("/user/request")
    public String getRequest(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error";
		}
		
		model.addAttribute("viewrequests", viewAuthorizationService.getPendingAuthorization(user));
		
        return "external/accessrequests";
    }
	
	@GetMapping("/user/request/view/{id}")
    public String getRequest(@PathVariable UUID id, Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/login";
		}
		
		ViewAuthorization authorization = viewAuthorizationService.getAuthorizationById(id);
		if (authorization == null) {
			return "redirect:/error?code=404";
		}
		if (authorization.getExternal() != user) {
			return "redirect:/error?code=401";
		}
		
		model.addAttribute("viewrequest", authorization);
		
        return "external/accessrequest_detail";
    }
	
	@PostMapping("/user/request/{id}")
    public String getRequests(@PathVariable UUID id, @ModelAttribute ViewAuthorization request, BindingResult bindingResult) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/login";
		}
		String status = request.getStatus();
		if (status == null || !(status.equals("approved") || status.equals("rejected"))) {
			return "redirect:/error?code=400";
		}
		
		ViewAuthorization authorization = viewAuthorizationService.getAuthorizationById(id);
		if (authorization == null) {
			return "redirect:/error?code=404";
		}
		if (authorization.getExternal() != user) {
			return "redirect:/error?code=401";
		}
		
		authorization.setStatus(status);
		authorization = viewAuthorizationService.approveAuthorization(authorization);
		
        return "redirect:/user/request";
    }
}
