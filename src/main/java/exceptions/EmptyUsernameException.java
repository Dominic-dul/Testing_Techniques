package exceptions;

public class EmptyUsernameException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "The username is empty";
	}
}
