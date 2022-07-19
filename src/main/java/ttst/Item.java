package ttst;

import java.util.ArrayList;

import exceptions.AuctionAlreadyPresentException;
import exceptions.AuctionNotOfTheItemException;

/*
 * This class represents an Item. Each Item can be added to the system by Sellers.
 * It can be put into Auctions and it can be won by other Users (through the Auctions).
*/
public class Item {

	private User owner;
	private ArrayList<Auction> auctions;

	private long itemId; //starts from 1
	private String description;
	private double bidIncrement;
	private ArrayList<Image> images;
	private int category;

	private static long idGenerator = 0;
	private static ArrayList<Item> items = new ArrayList<Item>();

	/*
	 * In the constructor is set the description of the Item, the bid increment needed to place a new bid,
	 * a list of images representing the item, a category for the Item
	 */
	public Item(String description, double bidIncrement, ArrayList<Image> images, int category) {
		this.itemId = ++idGenerator;

		this.auctions = new ArrayList<Auction>();

		this.description = description;
		this.bidIncrement = bidIncrement;
		this.images = images;
		this.category = category;
		this.owner = null;

		items.add(this);
	}


	/*
	 * Adds an Auction to the list of Auctions of the Item
	 */
	public void addAuction(Auction auction) throws AuctionNotOfTheItemException, AuctionAlreadyPresentException {
		if (auction.getItem() != this) {
			throw new AuctionNotOfTheItemException();
		} else if (auctions.contains(auction)) {
			throw new AuctionAlreadyPresentException();
		} else {
			this.auctions.add(auction);
		}
	}

	public ArrayList<Auction> getAuctions() {
		return auctions;
	}

	public void setAuctions(ArrayList<Auction> auctions) {
		this.auctions = auctions;
	}

	/*
	 * Gets the Item with the corresponding id
	 */
	public static Item getItemById(long itemId) {

		ArrayList<Item> it = Item.getItems();

		Item item = null;

		for (int i = 0; i < it.size(); i++) {
			if (itemId == it.get(i).getItemId()) {
				item = it.get(i);
			}
		}

		return item;
	}

	public static ArrayList<Item> getItems() {
		return items;
	}

	public long getItemId() {
		return itemId;
	}

	public User getOwner() {
		return this.owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getBidIncrement() {
		return bidIncrement;
	}

	public void setBidIncrement(double bidIncrement) {
		this.bidIncrement = bidIncrement;
	}

	public ArrayList<Image> getImages() {
		return images;
	}

	public void setImages(ArrayList<Image> photos) {
		this.images = photos;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	/*
	 * Returns an instance of an Item
	 */
	public static Item getInstance() {

		ArrayList<Image> images = new ArrayList<Image>();
		images.add(new Image("file1"));
		images.add(new Image("file2"));

		return new Item("smartphone", 3, images, 3);
	}

}
