package exceptions;

public class InvalidDescriptionException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "The description is not valid";
	}
}
