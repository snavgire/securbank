/**
 * 
 */
package securbank.controller;

import java.util.List;
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

import securbank.models.Transaction;
import securbank.models.Transfer;
import securbank.models.User;
import securbank.services.AccountService;
import securbank.services.OtpService;
import securbank.services.TransactionService;
import securbank.services.TransferService;
import securbank.services.UserService;
import securbank.validators.NewTransactionFormValidator;
import securbank.validators.NewTransferFormValidator;
import securbank.validators.EditUserFormValidator;
import securbank.validators.NewMerchantPaymentFormValidator;
import securbank.validators.NewUserFormValidator;


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
	private AccountService accountService;
	
	@Autowired
	NewTransferFormValidator transferFormValidator;
	
	@Autowired
	public HttpSession session;

	@Autowired 
	NewUserFormValidator userFormValidator;
	
	@Autowired 
	EditUserFormValidator editUserFormValidator;
	
	@Autowired
	NewMerchantPaymentFormValidator merchantPaymentFormValidator;
	
	@Autowired
	OtpService otpService;
	
	final static Logger logger = LoggerFactory.getLogger(ExternalUserController.class);

	@GetMapping("/user/details")
    public String currentUserDetails(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error?code=400&path=user-notfound";
		}
		
		model.addAttribute("user", user);
		logger.info("GET request: External user detail");
		
        return "external/detail";
    }
	
	@GetMapping("/user/createtransaction")
	public String newTransactionForm(Model model){
		model.addAttribute("transaction", new Transaction());
		logger.info("GET request: Extrernal user transaction creation request");
		
		return "external/createtransaction";
	}
	
	@GetMapping("/user/transaction/otp")
	public String createTransactionOtp(Model model){
		model.addAttribute("transaction", new Transaction());
		logger.info("GET request: Extrernal user transaction generate OTP");
		User currentUser = userService.getCurrentUser();
		otpService.createOtpForUser(currentUser);
		
		return "redirect:/user/createtransaction";
	}
	
	@PostMapping("/user/createtransaction")
    public String submitNewTransaction(@ModelAttribute Transaction transaction, BindingResult bindingResult) {
		logger.info("POST request: Submit transaction");
		
		transactionFormValidator.validate(transaction, bindingResult);
		
		String otp = otpService.getOtpByUser(transaction.getAccount().getUser()).getCode();
		 if(!transaction.getOtp().equals(otp)){
			logger.info("Otp mismatch");
			return "redirect:/error?code=400&path=transfer-error";
		 }
		
		if(bindingResult.hasErrors()){
			logger.info("POST request: createtransaction form with validation errors");
			return "redirect:/error?code=400&path=transaction-error";
		}
		
		if(transaction.getType().contentEquals("CREDIT")){
			if (transactionService.initiateCredit(transaction) == null) {
				return "redirect:/error?code=400&path=transaction-error";
			}
		}
		else {
			if (transactionService.initiateDebit(transaction) == null) {
				return "redirect:/error?code=400&path=transaction-error";
			}
		}
		
		//deactivate current otp
		otpService.deactivateOtpByUser(userService.getCurrentUser());
		
		return "redirect:/user/createtransaction";
    }
	
	@GetMapping("/user/createtransfer")
	public String newTransferForm(Model model){
		model.addAttribute("transfer", new Transfer());
		logger.info("GET request: Extrernal user transfer creation request");
		
		return "external/createtransfer";
	}

	@GetMapping("/user/transfer/otp")
	public String createTransferOtp(Model model){
		model.addAttribute("transfer", new Transfer());
		logger.info("GET request: Extrernal user transfer generate OTP");
		User currentUser = userService.getCurrentUser();
		otpService.createOtpForUser(currentUser);
		
		return "redirect:/user/createtransfer";
	}
	
	@PostMapping("user/createtransfer")
    public String submitNewTransfer(@ModelAttribute Transfer transfer, BindingResult bindingResult) {
		logger.info("POST request: Submit transfer");
		
		transferFormValidator.validate(transfer, bindingResult);
		
		String otp = otpService.getOtpByUser(userService.getCurrentUser()).getCode();
		 if(!transfer.getOtp().equals(otp)){
			logger.info("Otp mismatch");
			return "redirect:/error?code=400&path=transfer-error";
		 }
		
		if(bindingResult.hasErrors()){
			logger.info("POST request: createtransfer form with validation errors");
			return "redirect:/error?code=400&path=transfer-error";
		}
		
		if(transferService.initiateTransfer(transfer)==null){
			return "redirect:/error?code=400&path=transfer-error";
		}
		
		//deactivate current otp
		otpService.deactivateOtpByUser(userService.getCurrentUser());
		
		return "redirect:/user/createtransfer";
	}

	@GetMapping("/user/merchant/payment")
	public String newMerchantTransferForm(Model model){
		model.addAttribute("transfer", new Transfer());
		logger.info("GET request: Extrernal user transfer creation request");
		
		return "external/merchantpayment";
	}

	@GetMapping("/user/merchanttransfer/otp")
	public String createMerchantTransferOtp(Model model){
		model.addAttribute("transfer", new Transfer());
		logger.info("GET request: Extrernal user transfer generate OTP");
		User currentUser = userService.getCurrentUser();
		otpService.createOtpForUser(currentUser);
		
		return "redirect:/user/merchant/payment";
	}
	
	@PostMapping("/user/merchant/payment")
    public String submitNewMerchantTransfer(@ModelAttribute Transfer transfer, BindingResult bindingResult) {
		logger.info("POST request: Submit transfer");
		
		merchantPaymentFormValidator.validate(transfer, bindingResult);
		
		String otp = otpService.getOtpByUser(userService.getCurrentUser()).getCode();
		 if(!transfer.getOtp().equals(otp)){
			logger.info("Otp mismatch");
			return "redirect:/error?code=400&path=transfer-error";
		 }
		
		if(bindingResult.hasErrors()){
			logger.info("POST request: createtransfer form with validation errors");
			return "redirect:/error?code=400&path=transfer-error";
		}
		
		if(transferService.initiateMerchantPaymentRequest(transfer)==null){
			return "redirect:/error?code=400&path=transfer-error";
		}
		
		//deactivate current otp
		otpService.deactivateOtpByUser(userService.getCurrentUser());
		
		return "redirect:/user/merchant/payment";
	}
	
	@GetMapping("/user/edit")
    public String editUser(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error";
		}
		model.addAttribute("user", user);
		
        return "external/edit";
    }
	
	@PostMapping("/user/edit")
    public String editSubmit(@ModelAttribute User user, BindingResult bindingResult) {
		editUserFormValidator.validate(user, bindingResult);
		if (bindingResult.hasErrors()) {
			return "external/edit";
        }
		
		// create request
    	userService.createExternalModificationRequest(user);
	
        return "redirect:/";
    }
	
	@GetMapping("/user/transfers")
    public String getTransfers(Model model) {
		logger.info("GET request:  All pending transfers");
		
		List<Transfer> transfers = transferService.getTransfersByStatusAndUser(userService.getCurrentUser(),"Waiting");
		if (transfers == null) {
			return "redirect:/error?code=404&path=transfers-not-found";
		}
		model.addAttribute("transfers", transfers);
		
        return "external/pendingtransfers";
    }
	
	@PostMapping("/user/transfer/request/{id}")
    public String approveRejectTransfer(@ModelAttribute Transfer trans, @PathVariable() UUID id, BindingResult bindingResult) {
		
		Transfer transfer = transferService.getTransferById(id);
		if (transfer == null) {
			return "redirect:/error?code=404&path=request-invalid";
		}
		
		// checks if user is authorized for the request to approve
		if (!transfer.getFromAccount().getUser().getEmail().equalsIgnoreCase(userService.getCurrentUser().getEmail())) {
			logger.warn("Transafer made TO non external account");
			return "redirect:/error?code=401&path=request-unauthorised";
		}
		
		if (!transfer.getToAccount().getUser().getRole().equalsIgnoreCase("ROLE_MERCHANT")) {
			logger.warn("Transafer made FROM non merchant account");
					
			return "redirect:/error?code=401&path=request-unauthorised";
		}
		
		if("approved".equalsIgnoreCase(trans.getStatus())){
			//check if transfer is valid in case modified
			if(transferService.isTransferValid(transfer)==false){
				return "redirect:/error?code=401&path=amount-invalid";
			}
			transferService.approveTransferToPending(transfer);
		}
		else if ("rejected".equalsIgnoreCase(trans.getStatus())) {
			transferService.declineTransfer(transfer);
		}
		
		logger.info("GET request: Manager approve/decline external transaction requests");
		
        return "redirect:/user/transfers";
    }
	
	@GetMapping("/user/transfer/{id}")
    public String getTransferRequest(Model model, @PathVariable() UUID id) {
		Transfer transfer = transferService.getTransferById(id);
		
		if (transfer == null) {
			return "redirect:/error?code=404&path=request-invalid";
		}

		// checks if user is authorized for the request to approve
		if (!transfer.getFromAccount().getUser().getEmail().equalsIgnoreCase(userService.getCurrentUser().getEmail())) {
			logger.warn("Transafer made TO non external account");
			return "redirect:/error?code=401&path=request-unauthorised";
		}
		
				
		if (!transfer.getToAccount().getUser().getRole().equalsIgnoreCase("ROLE_MERCHANT")) {
			logger.warn("Transafer made FROM non merchant account");
					
			return "redirect:/error?code=401&path=request-unauthorised";
		}
				
		model.addAttribute("transfer", transfer);
		logger.info("GET request: User merchant transfer request by ID");
		
        return "external/approverequests";
	}
}
