package exceptions;

public class InOpenAuctionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "The item is in an ongoing auction";
	}
}
