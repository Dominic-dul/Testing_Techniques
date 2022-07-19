package exceptions;

public class UnexistingUserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "The user does not exist";
	}
}
