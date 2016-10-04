package securbank.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Ayush Gupta
 *
 */
@Controller
public class AdminDashCreateAccount {
	
	@RequestMapping("/admindash_create_account")
    public String adminController(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
       return "admindash_create_account";
    }
}
