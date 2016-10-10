package securbank.services;

import securbank.models.Account;
import securbank.models.CreditCard;

public interface CreditCardService {
	/**
	 * Creates a new credit card if there is not already
	 * 
	 * @param accountId
	 *            The id of the account the credit card will be under.
	 * @param apr
	 *            The annual percentage rate (APR) of the credit card.
	 * @param maxLimit
	 *            The maximum balancethe issuer allows on the credit card.
	 * @return The newly created credit card.
	 */
	public CreditCard createCreditCard(Account account, double apr, double maxLimit);

	/**
	 * Generates the daily interest using the following formula:
	 * 
	 * Interest = Balance * APR / 365.
	 * 
	 * The interest should be added to the balance at the beginning of each day.
	 * 
	 * Source:
	 * https://www.discover.com/credit-cards/resources/interest-and-aprs/how-does-my-credit-card-interest-work
	 * 
	 * @param creditCard
	 *            The card to generate the interest from.
	 * @return The generated interest.
	 */
	public double generateInterest(CreditCard creditCard);

	/**
	 * Retrieves the details of the credit card under the given account.
	 * 
	 * @param accountId
	 *            The id of the account the credit card is under.
	 * @return The credit card details.
	 */
	public CreditCard getCreditCardDetails(Account account);
}
