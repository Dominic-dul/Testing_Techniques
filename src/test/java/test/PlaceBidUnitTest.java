package test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.ThrowingConsumer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import exceptions.AuctionExpiredException;
import exceptions.InvalidAuctionIdException;
import exceptions.LowPriceException;
import exceptions.NotOpenedAuctionException;
import ttst.Auction;
import ttst.Item;
import ttst.User;

public class PlaceBidUnitTest {
	
	private final long MINUTE = 60000;
	private final long HOUR = 3600000;
	private final long DAY = 86400000;
	private final long WEEK = 604800000;

    private User user = new User("nickname", "password");
    

    public Auction initializeAuction(long auctionStartedBefore, double startPrice, long auctionDuration){
        return new Auction(
                new Item(
                        "Test item", //description
                        0, //bidIncrement
                        null, //images
                        0 //category
                ),
                startPrice, //startPrice
                System.currentTimeMillis() - auctionStartedBefore, //startTime
                System.currentTimeMillis() + auctionDuration //endTime
        );
    }
    
    /// Deprecated
    
    
    @Test
    void invalidAuctionIDTest(){
        assertThrows(InvalidAuctionIdException.class, () -> user.placeBid(25648,11, false));
    }

    @ParameterizedTest
    @ValueSource(longs = {-10, -MINUTE, -HOUR, -DAY, -WEEK})
    void notOpenedAuctionTest(long auctionStartTime) {
        Auction auction = initializeAuction(auctionStartTime, 10, 10000);
        
        assertThrows(NotOpenedAuctionException.class, () -> user.placeBid(auction.getId(),11, false));
    }
    
    @ParameterizedTest
    @ValueSource(longs = {10, MINUTE, HOUR, DAY, WEEK})
    void openedAuctionTest(long auctionStartTime) throws InvalidAuctionIdException, NotOpenedAuctionException, LowPriceException, AuctionExpiredException   {
        Auction auction = initializeAuction(auctionStartTime, 10, 10000);

        assertTrue(user.placeBid(auction.getId(), 11, false));
    }

    @ParameterizedTest
    @ValueSource(longs = {-10, -MINUTE, -HOUR, -DAY, -WEEK})
    void auctionExpiredTest(long duration) {
        Auction auction = initializeAuction(1000, 10 ,duration);

        assertThrows(AuctionExpiredException.class, () -> user.placeBid(auction.getId(),11, false));
        
    }
    
    @ParameterizedTest
    @ValueSource(longs = {10, MINUTE, HOUR, DAY, WEEK})
    void auctionHasNotExpiredTest(long duration) throws InvalidAuctionIdException, NotOpenedAuctionException, LowPriceException, AuctionExpiredException {
        Auction auction = initializeAuction(1000, 10 ,duration);
        
        assertTrue(user.placeBid(auction.getId(),11, false));
    }

    @ParameterizedTest
    @ValueSource(doubles = {10, 10.1, 10.5, 20, 50})
    void checkValidPriceTest(double newBid) throws InvalidAuctionIdException, NotOpenedAuctionException, LowPriceException, AuctionExpiredException {
        Auction auction = initializeAuction(1000, 10 ,10000);
        
        assertTrue(user.placeBid(auction.getId(), newBid, false));

    }
    
    @ParameterizedTest
    @ValueSource(doubles = {9.9, 9.5, 8, 5, 1, 0})
    void checkInvalidPriceTest(double newBid){
        Auction auction = initializeAuction(1000, 10 ,10000);
        
        assertThrows(LowPriceException.class, () -> user.placeBid(auction.getId(), newBid, false));
    }
    
    
    
    @TestFactory
	public Stream<DynamicTest> multipleBidPlacementsTest() {

		Item testItem = new Item("Test item",//description
				50, //bidIncrement
				null, //images
				0 //category
				);
		
		Auction auction = new Auction(
				testItem,
				10, //starting price
				System.currentTimeMillis() - 1000, //startTime
				System.currentTimeMillis() + 10000 //endTime
			);
		
		Iterator<Double> randomIntGeneratorForNextBid = new Iterator<Double>() {
			int counter = 50; //how many times to do the test
			double currentPrice = auction.getStartPrice(); //the starting price to bet on
			
			@Override
			public boolean hasNext() {
				return counter > 0;
			}
			
			@Override
			public Double next() {
				Random random = new Random();
				double randomDouble = random.nextDouble() + testItem.getBidIncrement(); 
				counter--;
				return currentPrice += randomDouble; //new bid
			}
		};

		Function<Double, String> displayAuctionBid = bidValue -> "currentBid: " + bidValue;
		ThrowingConsumer<Double> placeBid = bidValue -> assertTrue(user.placeBid(auction.getId(), bidValue, false));
		return DynamicTest.stream(randomIntGeneratorForNextBid, displayAuctionBid, placeBid);
	}
}
