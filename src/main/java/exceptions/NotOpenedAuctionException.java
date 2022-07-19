package exceptions;

public class NotOpenedAuctionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "The auction has not yet started";
	}
}
