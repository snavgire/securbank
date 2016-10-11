package securbank.controller;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import securbank.models.User;

/**
 * @author Ayush Gupta
 *
 */
@Controller
public class SignUp {

	@RequestMapping("/signup")
	public String signupExternalAccount(Model model) {
		User user = new User();
		model.addAttribute("user",user);
		return "signup";
	}

	@RequestMapping(value = "/signupExternalUser", method = RequestMethod.POST)
	public String signupExternalUserAccount(@ModelAttribute(value = "user") User user) {
		System.out.println("Username: " + user.getUsername());
		return "signup";
	}
}
