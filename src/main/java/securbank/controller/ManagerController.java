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

import securbank.models.ModificationRequest;
import securbank.models.User;
import securbank.services.UserService;
import securbank.validators.InternalEditUserFormValidator;

/**
 * @author Ayush Gupta
 *
 */
@Controller
public class ManagerController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private InternalEditUserFormValidator editUserFormValidator;
	
	@GetMapping("/manager/details")
    public String currentUserDetails(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error?code=user-notfound";
		}
		
		model.addAttribute("user", user);
			
        return "manager/detail";
    }
	
	@GetMapping("/manager/edit")
    public String editUser(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error";
		}
		model.addAttribute("user", user);
		
        return "manager/edit";
    }
	
	@PostMapping("/manager/edit")
    public String editSubmit(@ModelAttribute ModificationRequest request, BindingResult bindingResult) {
		editUserFormValidator.validate(request, bindingResult);
		if (bindingResult.hasErrors()) {
			return "manager/edit";
        }
		
		// create request
    	userService.createInternalModificationRequest(request);
	
        return "redirect:/";
    }
	
	@GetMapping("/manager/user/request")
    public String getAllUserRequest(Model model) {
		List<ModificationRequest> modificationRequests = userService.getModificationRequests("pending", "external");
		if (modificationRequests == null) {
			model.addAttribute("modificationrequests", new ArrayList<ModificationRequest>());
		}
		else {
			model.addAttribute("modificationrequests", modificationRequests);	
		}
		
        return "manager/modificationrequests";
    }
	
	@GetMapping("/manager/user/request/{id}")
    public String getUserRequest(Model model, @PathVariable() UUID id) {
		ModificationRequest modificationRequest = userService.getModificationRequest(id);
		
		if (modificationRequest == null) {
			return "redirect:/error?=code=400&path=request-invalid";
		}
		
		model.addAttribute("modificationrequest", modificationRequest);
    	
        return "manager/modificationrequest_detail";
    }
	
	@PostMapping("/manager/user/request/{requestId}")
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
		
		// checks if manager is authorized for the request to approve
		if (!request.getUserType().equals("external")) {
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
		
        return "redirect:/manager/user/request";
    }
}
