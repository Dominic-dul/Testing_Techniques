package ttst;

import java.util.ArrayList;

import exceptions.AuctionAlreadyPresentException;
import exceptions.AuctionNotOfTheItemException;
import exceptions.InOpenAuctionException;
import exceptions.InvalidBidIncrementException;
import exceptions.InvalidCategoryException;
import exceptions.InvalidDescriptionException;
import exceptions.InvalidOwnerException;
import exceptions.InvalidPriceException;
import exceptions.InvalidTimeException;

/*
 * This class represents Sellers. A Seller is a special User that can add Items to the system,
 * modify Items and create Auctions for specific Items.
 */
public class Seller extends User {

	private static Seller sellerSingleton;

	
	/*
	 * Returns a Singleton of a Seller
	 */
	public static Seller getInstance() {
		if (sellerSingleton == null) {
			sellerSingleton = new Seller("sellerUsername", "password");
		}
		return sellerSingleton;
	}

	/*
	 * Constructor with the username and password of the Seller
	 */
	public Seller(String username, String password) {
		super(username, password);

	}

	/*
	 * It adds an Item to the System. The description of the Item, the bid increment needed to place a new bid,
	 * a list of images representing the item and a category for the Item need to be specified.
	 */
	public boolean addItem(String description, double bidIncrement, ArrayList<Image> images, int categoryId)
			throws InvalidDescriptionException, InvalidBidIncrementException, InvalidCategoryException {

		Item item = null;

		if (description == null || description.equals("") || description.length() > 200) {
			throw new InvalidDescriptionException();
		} else if (bidIncrement <= 0) {
			throw new InvalidBidIncrementException();
		} else if (categoryId < 0 || categoryId >= Category.getCategories().length) {
			throw new InvalidCategoryException();
		} else {
			item = new Item(description, bidIncrement, images, categoryId);

			this.getOwnedItems().add(item);

			item.setOwner(this);
		}

		return this.getOwnedItems().contains(item);

	}
/*
 * It allows to modify the description of the Item, the bid increment needed to place a new bid,
	 * the list of images representing the item and the category of the Item.
 */
	public boolean modifyItem(long itemId, String description, double bidIncrement, ArrayList<Image> images,
			int categoryId) throws InvalidDescriptionException, InvalidCategoryException, InvalidBidIncrementException,
			InvalidOwnerException, InOpenAuctionException {

		Item item = Item.getItemById(itemId);

		boolean inAuction = false;

		ArrayList<Auction> auctions = item.getAuctions();

		for (int i = 0; i < auctions.size(); i++) {
			if (auctions.get(i).getEndTime() > System.currentTimeMillis())
				inAuction = true;
		}

		if (this != item.getOwner()) {
			throw new InvalidOwnerException();
		} else if (description == null || description.equals("") || description.length() > 200) {
			throw new InvalidDescriptionException();
		} else if (bidIncrement <= 0) {
			throw new InvalidBidIncrementException();
		} else if (categoryId < 0 || categoryId >= Category.getCategories().length) {
			throw new InvalidCategoryException();
		} else if (inAuction) {
			throw new InOpenAuctionException();
		} else {
			item.setDescription(description);
			item.setBidIncrement(bidIncrement);
			item.setImages(images);
			item.setCategory(categoryId);
		}

		return item.getDescription().equals(description) || item.getBidIncrement() - bidIncrement < 0.001
				|| item.getImages() == images || item.getCategory() == categoryId;

	}

	/*
	 * It creates an Auction for the specified Item. A start price of the Auction, a start time and an end time
	 * must be specified.
	 */
	public boolean createAuction(long itemId, double startPrice, long startTime, long endTime)
			throws InvalidOwnerException, InvalidPriceException, InvalidTimeException {

		Item item = Item.getItemById(itemId);

		Auction auction = null;

		ArrayList<Auction> auctions = item.getAuctions();
		long minStartTime = System.currentTimeMillis();

		for (int i = 0; i < auctions.size(); i++) {
			if (auctions.get(i).getEndTime() > minStartTime) {
				minStartTime = auctions.get(i).getEndTime();
			}
		}

		if (item.getOwner() != this) {
			throw new InvalidOwnerException();
		} else if (startPrice < 0) {
			throw new InvalidPriceException();
		} else if (startTime <= minStartTime || endTime <= startTime || startTime < System.currentTimeMillis()) {
			System.out.println(startTime <= minStartTime);
			throw new InvalidTimeException();
		} else {

			auction = new Auction(item, startPrice, startTime, endTime);
			try {
				item.addAuction(auction);
			} catch (AuctionNotOfTheItemException e) {
				System.out.println(e.toString());
				e.printStackTrace();
			} catch (AuctionAlreadyPresentException e) {
				System.out.println(e.toString());
				e.printStackTrace();
			}

		}

		return Auction.getAuctions().contains(auction) && item.getAuctions().contains(auction);
	}

}
