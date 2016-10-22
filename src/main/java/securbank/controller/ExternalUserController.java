/**
 * 
 */
package securbank.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import securbank.models.Transaction;
import securbank.models.Transfer;
import securbank.models.User;
import securbank.services.TransactionService;
import securbank.services.TransferService;
import securbank.services.UserService;
import securbank.validators.NewTransactionFormValidator;
import securbank.validators.NewTransferFormValidator;

/**
 * @author Ayush Gupta
 *
 */
@Controller
public class ExternalUserController {
	@Autowired
	UserService userService;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	NewTransactionFormValidator transactionFormValidator;
	
	@Autowired
	private TransferService transferService;
	
	@Autowired
	NewTransferFormValidator transferFormValidator;
	
	@Autowired
	public HttpSession session;
	
	final static Logger logger = LoggerFactory.getLogger(ExternalUserController.class);
	
	@GetMapping("/user/details")
    public String currentUserDetails(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error?code=user-notfound";
		}
		
		model.addAttribute("user", user);
		logger.info("GET request: External user detail");
		
        return "external/detail";
    }
	
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
	
	@GetMapping("/external/createtransfer")
	public String newTransferForm(Model model){
		model.addAttribute("transfer", new Transfer());
		logger.info("GET request: Extrernal user transfer creation request");
		return "external/createtransfer";
	}
	
	@PostMapping("external/createtransfer")
    public String submitNewTransfer(@ModelAttribute Transfer transfer, BindingResult bindingResult) {
		logger.info("POST request: Submit transaction");
		
		transferFormValidator.validate(transfer, bindingResult);
		
		/*if(bindingResult.hasErrors()){
			return "redirect:/";
		}*/
		
		if(transferService.initiateTransfer(transfer)==null){
			return "redirect:/";
		}
		
		return "external/createtransfer";
    }
}
