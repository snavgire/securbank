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
public class AdminDashboardController {

	@RequestMapping("/admindash_view_accounts")
	public String adminViewAccountsController(ModelMap modelMap) {
		ArrayList<User> userList = new ArrayList<>();

		User user1 = new User();
		user1.setUserId(UUID.randomUUID());
		user1.setUsername("nloney");
		user1.setFirstName("Nikhil");
		user1.setLastName("Loney");
		user1.setMiddleName("Antony");
		user1.setEmail("nloney@asu.edu");

		User user2 = new User();
		user2.setUserId(UUID.randomUUID());
		user2.setUsername("rloney");
		user2.setFirstName("Rahul");
		user2.setLastName("Loney");
		user2.setMiddleName("Antony");
		user2.setEmail("rloney@asu.edu");

		User user3 = new User();
		user3.setUserId(UUID.randomUUID());
		user3.setUsername("xyz");
		user3.setFirstName("X");
		user3.setLastName("Y");
		user3.setMiddleName("Z");
		user3.setEmail("xyz@asu.edu");

		User user4 = new User();
		user4.setUserId(UUID.randomUUID());
		user4.setUsername("roro");
		user4.setFirstName("Rohan");
		user4.setLastName("Joshi");
		user4.setMiddleName("Hilarious");
		user4.setEmail("roro@asu.edu");

		User user5 = new User();
		user5.setUserId(UUID.randomUUID());
		user5.setUsername("jfallon");
		user5.setFirstName("Jimmy");
		user5.setLastName("Fallon");
		user5.setMiddleName("Irish");
		user5.setEmail("jfallon@asu.edu");

		User user6 = new User();
		user6.setUserId(UUID.randomUUID());
		user6.setUsername("cr7");
		user6.setFirstName("Cristiano");
		user6.setLastName("Ronaldo");
		user6.setMiddleName("Devil");
		user6.setEmail("cr7@asu.edu");

		User user7 = new User();
		user7.setUserId(UUID.randomUUID());
		user7.setUsername("leo");
		user7.setFirstName("Lionel");
		user7.setLastName("Messi");
		user7.setMiddleName("God");
		user7.setEmail("leo@asu.edu");

		User user8 = new User();
		user8.setUserId(UUID.randomUUID());
		user8.setUsername("mjordan");
		user8.setFirstName("Michael");
		user8.setLastName("Jordan");
		user8.setMiddleName("Legen");
		user8.setEmail("mjordan@asu.edu");

		userList.add(user1);
		userList.add(user2);
		userList.add(user3);
		userList.add(user4);
		userList.add(user5);
		userList.add(user6);
		userList.add(user7);
		userList.add(user8);

		modelMap.put("userlist", userList);

		return "admindash_view_accounts";
	}

	@RequestMapping("/admindash_system_logs")
	public String adminControllerSystemLogs(
			@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {
		return "admindash_system_logs";
	}

	@RequestMapping("/admindash_create_account")
	public String adminControllerCreateAccount(Model model) {
		User user = new User();
		model.addAttribute("user",user);
		return "admindash_create_account";
	}

	@RequestMapping(value = "/createInternalUser", method = RequestMethod.POST)
	public String adminControllerCreateUserAccount(@ModelAttribute(value = "user") User user) {
		System.out.println("Username: " + user.getUsername());
		return "admindash_create_account";
	}
}
