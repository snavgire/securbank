package securbank.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
	
	final static Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	@GetMapping("/employee/details")
    public String currentUserDetails(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error?code=user.notfound";
		}
		
		model.addAttribute("user", user);
		logger.info("GET request: Employee user detail");
		
        return "employee/detail";
    }
}
