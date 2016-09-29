package securbank.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import securbank.services.AuthenticationService;

/**
 * @author Ayush Gupta
 *
 */
@Controller
public class Home {
	@Autowired
	private AuthenticationService as; 
	@RequestMapping("/")
    public String homeController(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
       return "home";
    }
}
