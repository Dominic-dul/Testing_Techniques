package exceptions;

public class WrongPasswordException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "The password is wrong";
	}
}
