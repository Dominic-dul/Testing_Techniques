package ttst;

import java.util.ArrayList;

import exceptions.InvalidAuctionIdException;

/*
 * The Auction class is used to represent the auctions.
 * It also provides a static list of all the auctions.
 */
public class Auction {

	private long id;

	private Item item;
	private double startPrice;
	private long startTime;
	private long endTime;
	private ArrayList<Bid> bids;

	private double nextPrice;

	private static ArrayList<Auction> auctions = new ArrayList<Auction>();

	/*
	 * The constructor creates the auction, set the id (autoincrement) and add the auction to the list of auctions.
	 */
	public Auction() {
		if (auctions.size() > 0) {
			this.id = auctions.get(auctions.size() - 1).id + 1;
		} else {
			this.id = 0;
		}
		this.bids = new ArrayList<Bid>();

		auctions.add(this);
	}

	public Auction(Item item, double startPrice, long startTime, long endTime) {
		this();
		this.item = item;
		this.startPrice = startPrice;
		this.startTime = startTime;
		this.endTime = endTime;
		this.nextPrice = startPrice;
	}

	public static ArrayList<Auction> getAuctions() {
		return auctions;
	}

	/*
	 * Return the auction that corresponds to the provided id.
	 */
	public static Auction getAuction(long id) throws InvalidAuctionIdException {
		for (Auction auction : auctions) {
			if (auction.id == id) {
				return auction;
			}
		}
		throw new InvalidAuctionIdException();
	}

	
	/*
	 * Return instance
	 */
	public static Auction getInstance() {

		return new Auction();
	}

	/*
	 * Return next valid price for auction
	 */
	public double getNextPrice() {
		if(bids.size() == 0) {
			return startPrice;
		}
		
		return this.nextPrice;
	}

	public void setNextPrice(double d) {

		this.nextPrice = d;
	}

	public void setStartPrice(double d) {

		this.startPrice = d;
	}

	/*
	 * Return a boolean indicating whether or not the price is higher than the nextPrice
	 */
	public boolean checkPriceValidity(Double price) {
		return price >= this.nextPrice;
	}

	/*
	 * Check if the auction is open and not yet closed
	 */
	public boolean checkTimeValidity() {
		return hasStarted() && !hasExpired();
	}

	/*
	 * Check is the auction is open
	 */
	public boolean hasStarted() {
		return System.currentTimeMillis() > this.startTime;
	}

	/*
	 * Check if the auction is ended
	 */
	public boolean hasExpired() {
		return this.endTime < System.currentTimeMillis();
	}
	
	/*
	 * Add the bid to the list and update the next price
	 */
	public boolean addBid(Bid bid) {
		if (bids.add(bid)) {
			nextPrice = bid.getPrice() + item.getBidIncrement();
			return true;
		}
		return false;
	}

	public long getId() {
		return id;
	}

	public Item getItem() {
		return item;
	}

	public double getStartPrice() {
		return startPrice;
	}

	public long getStartTime() {
		return startTime;
	}
	

	public long getEndTime() {
		return endTime;
	}


	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
}
