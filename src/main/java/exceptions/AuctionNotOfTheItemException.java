package exceptions;

public class AuctionNotOfTheItemException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "It was tried to insert the auction to another item's list of auction";
	}
}
