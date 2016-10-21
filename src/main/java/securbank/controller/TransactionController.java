package securbank.controller;

import java.util.UUID;

import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.RequestParam;

import securbank.models.Account;
import securbank.models.NewUserRequest;
import securbank.models.Transaction;
import securbank.models.User;
import securbank.services.TransactionService;
import securbank.services.UserService;
import securbank.validators.NewTransactionFormValidator;

/**
 * 
 * @author Mitikaa Sama
 * @date Oct 14th
 *
 */
@Controller
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	NewTransactionFormValidator transactionFormValidator;
	
	@Autowired
	public HttpSession session;
	
	@Autowired
	UserService userService;
	
	final static Logger logger = LoggerFactory.getLogger(TransactionController.class);
	
	@GetMapping("/transaction/create")
	public String newTransactionForm(Model model){
		model.addAttribute("transaction", new Transaction());
		logger.info("GET request: Extrernal user transaction creation request");
		return "transaction/create";
	}
	
	@PostMapping("/transaction/create")
    public String submitNewTransaction(@ModelAttribute Transaction transaction, BindingResult bindingResult) {
		logger.info("POST request: Submit transaction");
		transactionFormValidator.validate(transaction, bindingResult);
		if(bindingResult.hasErrors()){
			return "redirect:/";
		}
		if(transaction.getType().contentEquals("CREDIT")){
			if (transactionService.initiateCredit(transaction) == null) {
				return "redirect:/";
			}
		}
		else {
			if (transactionService.initiateDebit(transaction) == null) {
				return "redirect:/";
			}
		}
		return "transaction/create";
    }
	
}
