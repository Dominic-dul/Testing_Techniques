package test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import exceptions.AuctionExpiredException;
import exceptions.InvalidAuctionIdException;
import exceptions.LowPriceException;
import exceptions.NotOpenedAuctionException;
import it.unibz.inf.bl.*;
import it.unibz.inf.be.*;

public class PlaceBidIntegrationTest {

	private static User seller;
	private static Item newItem;
	private Auction auction;
	private static ttst.User user;
	
	private final long MINUTE = 6;
	private final long HOUR = 3600000;
	private final long DAY = 86400000;
	private final long WEEK = 604800000;

	

	@BeforeAll
	public static void setUp() throws IOException {
		seller = UserManager.register("seller",
				"simple",
				"Mika",
				"Piazza",
				"3664473808",
				"email@email.com",
				"ASDFVC12S12F123S");

		ItemManager.loadCategoriesFromFile();
		newItem = ItemManager.createItem(seller.getUserID(), "1", "description", "2000");
		user = new ttst.User("buyer", "simple");
	}
	
	@Test
	public void ifInvalidAuctionIdThenThrowException() {
		auction = AuctionManager.openAuction(newItem.getItemID(), "2020-05-19 12:00:00",
				LocalDateTime.now().plusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), "1000");

		assertThrows(InvalidAuctionIdException.class, () -> user.placeBid(12, 2000, true));
	}
	

	@ParameterizedTest
    @ValueSource(doubles = {2000, 2000.1, 2000.5, 4000, 5000})
	public void ifValidAuctionIdThenPlaceBid(double newBid) throws IOException, InvalidAuctionIdException, NotOpenedAuctionException,
			LowPriceException, AuctionExpiredException {

		auction = AuctionManager.openAuction(newItem.getItemID(), "2020-05-19 12:00:00",
				LocalDateTime.now().plusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), "1000");

		user.placeBid(auction.getAuctionID(), newBid, true);
	}


	@ParameterizedTest
	@ValueSource(longs = {1, MINUTE, HOUR, DAY, WEEK})
	public void ifNotOpenedAuctionThenThrowException(long seconds) {
		auction = AuctionManager.openAuction(newItem.getItemID(),
				LocalDateTime.now().plusSeconds(seconds).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
				LocalDateTime.now().plusSeconds(seconds*10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), "1000");
	
		assertThrows(NotOpenedAuctionException.class, () -> user.placeBid(auction.getAuctionID(), 2000, true));
	}
	

	@ParameterizedTest
    @ValueSource(doubles = {999.9, 999.5, 80, 5, 1})
	public void ifLowerBidPriceThenThrowException(double newBid) {
		auction = AuctionManager.openAuction(newItem.getItemID(), "2020-05-19 12:00:00",
				LocalDateTime.now().plusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), "1000");

		assertThrows(LowPriceException.class, () -> user.placeBid(auction.getAuctionID(), newBid, true));
	}
	

	@ParameterizedTest
    @ValueSource(longs = {1, MINUTE, HOUR, DAY, WEEK})
	public void ifAuctionExpiredThenThrowException(long seconds) {
		auction = AuctionManager.openAuction(newItem.getItemID(),
				LocalDateTime.now().minusSeconds(seconds*10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
				LocalDateTime.now().minusSeconds(seconds).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), "1000");
		
		assertThrows(AuctionExpiredException.class, () -> user.placeBid(auction.getAuctionID(), 2000, true));
	}

}
