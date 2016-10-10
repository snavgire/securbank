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
import securbank.services.UserService;
import securbank.validators.NewUserFormValidator;
/**
 * @author Ayush Gupta
 *
 */
@Controller
public class CommonController {
	@Autowired
	UserService userService;
	
	@Autowired 
	NewUserFormValidator userFormValidator;
	
	final static Logger logger = LoggerFactory.getLogger(CommonController.class);
	
	@GetMapping("/signup")
    public String signupForm(Model model) {
		model.addAttribute("user", new User());
		logger.info("GET request: signup form");
		
		return "signup";
    }
		
	@PostMapping("/signup")
    public String signupSubmit(@ModelAttribute User user, BindingResult bindingResult) {
		userFormValidator.validate(user, bindingResult);
		if (bindingResult.hasErrors()) {
			logger.info("POST request: signup form with validation errors");
			
			return "signup";
        }
		
		logger.info("POST request: signup");
		
    	userService.createExternalUser(user);
    	
        return "redirect:/";
    }
	
	@GetMapping("/user/verify/{id}")
    public String verifyNewUser(Model model, @PathVariable UUID id) {
		if (userService.verifyNewUser(id) == false) {
			logger.info("GET request: verification failed of new external user");
			return "redirect:/error?code=400";
		}
		logger.info("GET request: verification of new external user");
		
		return "redirect:/";
    }
}
