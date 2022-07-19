package exceptions;

public class InvalidCategoryException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "The category does not exist";
	}
}
