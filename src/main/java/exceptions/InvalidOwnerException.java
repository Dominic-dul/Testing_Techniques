package exceptions;

public class InvalidOwnerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "The item is not owned by this user";
	}
}
