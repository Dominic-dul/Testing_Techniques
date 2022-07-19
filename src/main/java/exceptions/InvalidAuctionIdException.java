package exceptions;

public class InvalidAuctionIdException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "The auction id is not valid";
	}
}
