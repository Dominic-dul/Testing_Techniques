package exceptions;

public class LowPriceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "The price is too low";
	}
}
