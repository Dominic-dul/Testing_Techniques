package exceptions;

public class MaxPriceSmallerThanMinPriceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "The max price is smaller than the minimum price";
	}
}
