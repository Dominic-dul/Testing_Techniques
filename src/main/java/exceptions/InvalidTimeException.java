package exceptions;

public class InvalidTimeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "The time is not valid";
	}
}
