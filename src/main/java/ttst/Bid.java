package ttst;

/*
 * Class that represents the bid object.
 * It does not contain any main logic of the Bid system.
 * It has a price, a timestamp and a bidder user.
 */
public class Bid {
	private double price;
	private long time;
	private User bidder;
	
	public double getPrice() {
		return price;
	}
	
	public long getTime() {
		return time;
	}
	
	public User getBidder() {
		return bidder;
	}
	
	public Bid(double price, User bidder) {
		this.price = price;
		this.time = System.currentTimeMillis();
		this.bidder = bidder;
	}
}
