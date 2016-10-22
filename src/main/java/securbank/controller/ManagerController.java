/**
 * 
 */
package securbank.controller;

import java.util.List;
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

import securbank.models.Transaction;
import securbank.models.Transfer;
import securbank.models.User;
import securbank.services.AccountService;
import securbank.services.TransactionService;
import securbank.services.TransferService;
import securbank.services.UserService;

/**
 * @author Ayush Gupta
 *
 */
@Controller
public class ManagerController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private TransferService transferService;
	
	@Autowired
	private AccountService accountService;
	
	final static Logger logger = LoggerFactory.getLogger(ManagerController.class);
	
	@GetMapping("/manager/details")
    public String currentUserDetails(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error?code=400&path=user.notfound";
		}
		
		model.addAttribute("user", user);
		logger.info("GET request: Manager user detail");
			
        return "manager/detail";
    }
	
	@GetMapping("/manager/user")
    public String getUsers(Model model) {
		List<User> users = userService.getUsersByType("external");
		if (users == null) {
			return "redirect:/error?code=500";
		}
		model.addAttribute("users", users);
		logger.info("GET request:  All external users");

		return "manager/externalusers";
    }
	
	@GetMapping("/manager/user/{id}")
    public String getUserDetails(Model model, @PathVariable UUID id) {
		User user = userService.getUserByIdAndActive(id);
		if (user == null) {
			return "redirect:/error?code=400";
		}
		if (user.getType().equals("internal")) {
			logger.warn("GET request: Unauthorised request for internal user detail");
			
			return "redirect:/error?code=409";
		}
		
		model.addAttribute("user", user);
		logger.info("GET request:  External user detail by id");
		
        return "manager/userdetail";
    }
	
	@GetMapping("/manager/transactions")
    public String getTransactions(Model model) {

		List<Transaction> transactions = transactionService.getTransactionsByStatus("Pending");
		if (transactions == null) {
			return "redirect:/error?code=500";
		}
		model.addAttribute("transactions", transactions);
		logger.info("GET request:  All pending transactions");
		
        return "manager/pendingtransactions";
    }
	
	@PostMapping("/manager/transaction/request/{id}")
    public String approveRejectTransactions(@ModelAttribute Transaction trans, @PathVariable() UUID id, BindingResult bindingResult) {
		
		Transaction transaction = transactionService.getTransactionById(id);
		if (transaction == null) {
			return "redirect:/error?code=404&path=request-invalid";
		}
		
		// checks if manager is authorized for the request to approve
		if (!transaction.getAccount().getUser().getType().equalsIgnoreCase
				("external")) {
			logger.warn("GET request: Manager unauthrorised request access");
					
			return "redirect:/error?code=401&path=request-unauthorised";
		}
		
		if("approved".equalsIgnoreCase(trans.getApprovalStatus())){
			transactionService.approveTransaction(transaction);
		}
		else if ("rejected".equalsIgnoreCase(trans.getApprovalStatus())) {
			transactionService.declineTransaction(transaction);
		}
		
		logger.info("GET request: Manager approve/decline external transaction requests");
		
        return "manager/pendingtransactions";
    }
	
	@GetMapping("/manager/transaction/{id}")
    public String getTransactionRequest(Model model, @PathVariable() UUID id) {
		Transaction transaction = transactionService.getTransactionById(id);
		
		if (transaction == null) {
			return "redirect:/error?code=404&path=request-invalid";
		}

		// checks if manager is authorized for the request to approve
		if (!transaction.getAccount().getUser().getType().equals
				("external")) {
			logger.warn("GET request: Manager unauthrorised request access");
			
			return "redirect:/error?code=401&path=request-unauthorised";
		}
		
		model.addAttribute("transaction", transaction);
		logger.info("GET request: Manager external transaction request by ID");
		
        return "manager/approvetransaction";
    }
	
	@GetMapping("/manager/transfers")
    public String getTransfers(Model model) {
		logger.info("GET request:  All pending transfers");
		
		List<Transfer> transfers = transferService.getTransfersByStatus("Pending");
		if (transfers == null) {
			return "redirect:/error?code=500";
		}
		model.addAttribute("transfers", transfers);
		
        return "manager/pendingtransfers";
    }
	
	@PostMapping("/manager/transfer/request/{id}")
    public String approveRejectTransfer(@ModelAttribute Transfer trans, @PathVariable() UUID id, BindingResult bindingResult) {
		
		Transfer transfer = transferService.getTransferById(id);
		if (transfer == null) {
			return "redirect:/error?code=404&path=request-invalid";
		}
		
		//give error if account does not exist
		if (accountService.accountExists(transfer.getToAccount().getAccountNumber())) {
			logger.warn("TO account does not exist");	
			return "redirect:/error?code=401&path=request-invalid";
		}
		
		// checks if manager is authorized for the request to approve
		if (!transfer.getToAccount().getUser().getType().equalsIgnoreCase("external")) {
			logger.warn("Transafer made TO non external account");
			return "redirect:/error?code=401&path=request-unauthorised";
		}
		
		if (!transfer.getFromAccount().getUser().getType().equalsIgnoreCase("external")) {
			logger.warn("Transafer made FROM non external account");
					
			return "redirect:/error?code=401&path=request-unauthorised";
		}
		
		if("approved".equalsIgnoreCase(trans.getStatus())){
			transferService.approveTransfer(transfer);
		}
		else if ("rejected".equalsIgnoreCase(trans.getStatus())) {
			transferService.declineTransfer(transfer);
		}
		
		logger.info("GET request: Manager approve/decline external transaction requests");
		
        return "redirect:manager/pendingtransfers";
    }
	
	@GetMapping("/manager/transfer/{id}")
    public String getTransferRequest(Model model, @PathVariable() UUID id) {
		Transfer transfer = transferService.getTransferById(id);
		
		if (transfer == null) {
			return "redirect:/error?code=404&path=request-invalid";
		}

		// checks if manager is authorized for the request to approve
		if (!transfer.getToAccount().getUser().getType().equalsIgnoreCase("external")) {
			logger.warn("Transafer made TO non external account");		
			return "redirect:/error?code=401&path=request-unauthorised";
		}
				
		if (!transfer.getFromAccount().getUser().getType().equalsIgnoreCase("external")) {
			logger.warn("Transafer made FROM non external account");
			return "redirect:/error?code=401&path=request-unauthorised";
		}
				
		model.addAttribute("transfer", transfer);
		logger.info("GET request: Manager external transfer request by ID");
		
        return "manager/approvetransfer";
    }
}
