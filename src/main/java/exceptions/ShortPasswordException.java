package exceptions;

public class ShortPasswordException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "The password is too short";
	}
}
