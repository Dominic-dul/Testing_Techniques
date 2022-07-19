package ttst;

/*
 * Class specifying the possible category in which an Item can be put.
 */
public class Category {
	
//	Array of possible categories
	private static final String[] categories = {"Books", "Games", "Forniture", "Electronic", "Art"};
	
	public static String[] getCategories() {
		return categories;
	}

}
