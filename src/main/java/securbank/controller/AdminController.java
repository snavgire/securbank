/**
 * 
 */
package securbank.controller;

import java.util.ArrayList;
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

	@GetMapping("/admin/details")
    public String currentUserDetails(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error?code=user-notfound";
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
		if (userService.createNewUserRequest(newUserRequest) == null) {
			return "redirect:/error";
		};
    	
        return "redirect:/admin/user/add?success=true";
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
		
        return "admin/modificationrequests";
    }
	
	@GetMapping("/admin/user/request/{id}")
    public String getUserRequest(Model model, @PathVariable() UUID id) {
		ModificationRequest modificationRequest = userService.getModificationRequest(id);
		
		if (modificationRequest == null) {
			return "redirect:/error?=code=400&path=request-invalid";
		}
		
		model.addAttribute("modificationrequest", modificationRequest);
    	
        return "admin/modificationrequest_detail";
    }
	
	@PostMapping("/admin/user/request/{requestId}")
    public String approveEdit(@PathVariable UUID requestId, @ModelAttribute ModificationRequest request, BindingResult bindingResult) {
		String status = request.getStatus();
		if (status == null || !(request.getStatus().equals("approved") || !request.getStatus().equals("rejected"))) {
			return "redirect:/error?code=400&path=request-action-invalid";
		}
		request = userService.getModificationRequest(requestId);
		
		// checks validity of request
		if (request == null) {
			return "redirect:/error?code=400&path=request-invalid";
		}
		
		// checks if admin is authorized for the request to approve
		if (!request.getUserType().equals("internal")) {
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
		
        return "redirect:/admin/user/request";
    }
}
