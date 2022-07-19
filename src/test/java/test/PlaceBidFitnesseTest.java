package test;

import exceptions.AuctionExpiredException;
import exceptions.InvalidAuctionIdException;
import exceptions.LowPriceException;
import exceptions.NotOpenedAuctionException;
import fit.ColumnFixture;
import ttst.Auction;
import ttst.Bid;
import ttst.Item;
import ttst.User;

public class PlaceBidFitnesseTest extends ColumnFixture{
	
	private double newBid;
	private double startPrice;
	private long auctionDuration;
	private long auctionStartedBefore;
	private long auctionId;
	
	public void setAuctionStartedBefore(long auctionStartedBefore) {
		this.auctionStartedBefore = auctionStartedBefore;
	}
	
	public void setNewBid(double newBid) {
		this.newBid = newBid;
	}
	
	public void setStartPrice(double startPrice) {
		this.startPrice = startPrice;
	}

	public void setAuctionId(long auctionId) {
		this.auctionId = auctionId;
	}
	
	public void setAuctionDuration(long auctionDuration) {
		this.auctionDuration = auctionDuration;
	}
			

	public boolean invalidAuctionTest() {
		User user = new User("nickname", "password");
		Auction auction = new Auction(
				new Item(
						"Test item", //description
						0, //bidIncement
						null, //images
						0 //category
					),
					this.startPrice, //startPrice
					System.currentTimeMillis() - 1000, //startTime
					System.currentTimeMillis() + 10000 //endTime
				);
		
		try {
			user.placeBid(auctionId, newBid, false);
			return true;
		} catch (InvalidAuctionIdException | NotOpenedAuctionException |LowPriceException | AuctionExpiredException e) {
			return false;
		}
		
		
	}
	
	public boolean notOpenedAuctionTest() {
		User user = new User("nickname", "password");
		Auction auction = new Auction(
				new Item(
						"Test item", //description
						0, //bidIncement
						null, //images
						0 //category
					),
					this.startPrice, //startPrice
					System.currentTimeMillis() - auctionStartedBefore, //startTime
					System.currentTimeMillis() + 10000 //endTime
				);
		
		long currAuctionId = auction.getId();
		
		try {
			user.placeBid(currAuctionId, newBid, false);
			return true;
		} catch (InvalidAuctionIdException | NotOpenedAuctionException |LowPriceException | AuctionExpiredException e) {
			return false;
		}
		
	}
	
	public boolean auctionExpiredTest() {
		User user = new User("nickname", "password");
		Auction auction = new Auction(
				new Item(
						"Test item", //description
						0, //bidIncement
						null, //images
						0 //category
					),
					this.startPrice, //startPrice
					System.currentTimeMillis() - 1000, //startTime
					System.currentTimeMillis() + auctionDuration //endTime
				);
		
		long currAuctionId = auction.getId();
		
		try {
			user.placeBid(currAuctionId, newBid, false);
			return true;
		} catch (InvalidAuctionIdException | NotOpenedAuctionException |LowPriceException | AuctionExpiredException e) {
			return false;
		}
		
	}
	
	public boolean checkPriceValidityTest() {
		User user = new User("nickname", "password");
		Auction auction = new Auction(
				new Item(
						"Test item", //description
						0, //bidIncement
						null, //images
						0 //category
					),
					this.startPrice, //startPrice
					System.currentTimeMillis() - 1000, //startTime
					System.currentTimeMillis() + 10000 //endTime
				);
		
		long currAuctionId = auction.getId();
		
		try {
			user.placeBid(currAuctionId, newBid, false);
			return true;
		} catch (InvalidAuctionIdException | NotOpenedAuctionException |LowPriceException | AuctionExpiredException e) {
			return false;
		}
	}

}
