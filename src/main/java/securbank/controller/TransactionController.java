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
	
	@PostMapping("/user/transaction/create")
    public String createNewTransaction(@ModelAttribute Transaction transaction, BindingResult bindingResult) {
		UUID token = (UUID) session.getAttribute("verification.token");
		if(token == null){
			logger.info("POST request: Create new transaction with invalid session token");
			return "redirect:/error?code=400&path=bad-request";
		}
		else {
			// clears session
			session.removeAttribute("validation.token");
		}
		logger.info("POST request: Create new transaction");
		transactionFormValidator.validate(transaction, bindingResult);
		
		if(bindingResult.hasErrors()){
			return "/user/transaction/create";
		}
		
		if(transaction.getAmount()>5000){
			transaction.setCriticalStatus(true);
		}
		
		for (Account acc: userService.getCurrentUser().getAccounts()){
			if (acc.getType().equals("Checking")){
				transaction.setAccount(acc);
			}
		}
		
		if(transaction.getType()=="Credit"){
			if (transactionService.initiateCredit(transaction)== null) {
				return "redirect:/error?code=400&path=transaction-invalid";
			}
		}
		else {
			if (transactionService.initiateDebit(transaction)== null) {
				return "redirect:/error?code=400&path=transaction-invalid";
			}
		}
		 
		return "redirect:/transaction";
    }
	
}
