package exceptions;

public class InvalidBidIncrementException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "The bid increment is not valid";
	}
}
