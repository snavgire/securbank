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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import securbank.models.ModificationRequest;
import securbank.models.User;
import securbank.services.UserService;

/**
 * @author Ayush Gupta
 *
 */
@Controller
public class EmployeeController {
	@Autowired
	private UserService userService;
	
	@GetMapping("/employee/details")
    public String currentUserDetails(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error?code=user-notfound";
		}
		
		model.addAttribute("user", user);
		
        return "employee/detail";
    }
	
	@PostMapping("/employee/user/request/action")
    public String approveEdit(@RequestParam UUID requestId, @RequestParam String action, BindingResult bindingResult) {
		if (!action.equals("approve") && !action.equals("reject")) {
			return "redirect:/error?code=400&path=request-action-invalid";
		}
		
		ModificationRequest request = userService.getModificationRequest(requestId);
		// checks validity of request
		if (request == null) {
			return "redirect:/error?code=400&path=request-invalid";
		}
		
		// checks if employee is authorized for the request to approve
		if (!request.getUserType().equals("external")) {
			return "redirect:/error?code=401&path=request-unauthorised";
		}
		if (action.equals("approve")) {
			userService.approveModificationRequest(request);
		}
		// rejects request
		else {
			userService.rejectModificationRequest(request);
		}
		
        return "redirect:/employee/user/request";
    }
	
	@GetMapping("/employee/user/request")
    public String getAllUserRequest(Model model) {
		List<ModificationRequest> modificationRequests = userService.getAllPendingModificationRequest("external");
		if (modificationRequests == null) {
			model.addAttribute("modificationrequests", new ArrayList<ModificationRequest>());
		}
		else {
			model.addAttribute("modificationrequests", modificationRequests);	
		}
		
        return "userrequests";
    }
	
	@GetMapping("/employee/user/request/view")
    public String getUserRequest(Model model, @RequestParam UUID id) {
		ModificationRequest modificationRequest = userService.getModificationRequest(id);
		
		if (modificationRequest == null) {
			return "redirect:/error?=code=400&path=request-invalid";
		}
		
		model.addAttribute("modificationrequest", modificationRequest);
    	
        return "userrequest";
    }

}
