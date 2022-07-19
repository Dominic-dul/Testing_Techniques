package exceptions;

public class LongDescriptionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "The description is too long";
	}
}
