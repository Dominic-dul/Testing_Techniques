package exceptions;

public class EndDateBeforeStartDateException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "The end date is before the start date";
	}
}
