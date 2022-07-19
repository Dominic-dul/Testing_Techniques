package ttst;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import exceptions.AuctionExpiredException;
import exceptions.EmptyPasswordException;
import exceptions.EmptyUsernameException;
import exceptions.EndDateBeforeStartDateException;
import exceptions.InvalidAuctionIdException;
import exceptions.InvalidCategoryException;
import exceptions.LongDescriptionException;
import exceptions.LowPriceException;
import exceptions.MaxPriceSmallerThanMinPriceException;
import exceptions.NotOpenedAuctionException;
import exceptions.ShortPasswordException;
import exceptions.UnexistingUserException;
import exceptions.WrongPasswordException;
import it.unibz.inf.bl.AuctionManager;
import it.unibz.inf.bl.UserManager;
import it.unibz.inf.model.ModelFactory;

/*
 * This class represents the user, it is used to register/login the users.
 * It has a static list of all the users.
 * It keeps trace of the auction of interest and the owned items.
 * It has also singleton used in the testing phase.
 */
public class User {

	private static User userSingleton;

	public static ArrayList<User> users = new ArrayList<User>();

	private String username;
	private String password;
	private ArrayList<Item> ownedItems;
	private ArrayList<Auction> followedAuctions;
	
	private long id;

	private it.unibz.inf.be.User userApi;

	/*
	 * This method returns the singleton object, it is used in the tests
	 */
	public static User getInstance() {
		if (userSingleton == null) {
			userSingleton = new User("username", "password");
		}
		return userSingleton;
	}

	/*
	 * The constructor, it creates the user.
	 * The id is autoincremental
	 */
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		ownedItems = new ArrayList<Item>();
		followedAuctions = new ArrayList<Auction>();
		
		if (users.size() > 0) {
			this.id = users.get(users.size() - 1).id + 1;
		} else {
			this.id = 0;
		}

		
		users.add(this);
		
		this.userApi = UserManager.register(username, password, username, "Piazza", "3664473808", "email@email.com",
				"ASDFVC12S12F123P");
	}

/*
 * This method register the user, it has to check the inserted values and 
 * if there already exists an identical username.
 * It inserts the user in the list of users.
 */
	public static boolean register(String username, String password)
			throws EmptyPasswordException, EmptyUsernameException, ShortPasswordException {
		if (username == null || username.equals("")) {
			throw new EmptyUsernameException();
		}
		if (password == null || password.equals("")) {
			throw new EmptyPasswordException();
		}
		if (password.length() < 6) {
			throw new ShortPasswordException();
		}

		for (User user : users) {
			if (user.getUsername().equals(username)) {
				return false;
			}
		}
		new User(username, password);
		return true;
	}

	/*
	 * The place bid method has to place the bid (insert it in the list).
	 * It needs the auction id and a price, it does all the input validity checks
	 * and add the bid to the list of bids
	 */
	public boolean placeBid(long auctionId, double price, boolean isApiAuction)
			throws InvalidAuctionIdException, NotOpenedAuctionException, LowPriceException, AuctionExpiredException {
		
		if(isApiAuction) {
			it.unibz.inf.be.Auction auction;
			try {
				auction = ModelFactory.getModel().getAuctions().stream().filter(x -> x.getAuctionID() == (int) auctionId).findFirst().get();
			} catch (NoSuchElementException e) {
				throw new InvalidAuctionIdException();
			}
			
			if (auction.getStartDate().compareTo(Timestamp.valueOf(LocalDateTime.now())) > 0) {
				throw new NotOpenedAuctionException();
			}
			
			if (auction.getEndDate().compareTo(Timestamp.valueOf(LocalDateTime.now())) < 0) {
				throw new AuctionExpiredException();
			}
			
			it.unibz.inf.be.Bid bid = AuctionManager.placeBid(
					userApi.getUserID(), (int) auctionId, Timestamp.valueOf(LocalDateTime.now()), String.valueOf((int)price));
			
			if(AuctionManager.getMessage().equals("Please insert a higher bid")) {
	    		throw new LowPriceException();
	    	}
			
			return bid != null;
		}
		
		else {
			Auction auction = Auction.getAuction(auctionId);

			if (!auction.hasStarted()) {
				throw new NotOpenedAuctionException();
			}

			if (auction.hasExpired()) {
				throw new AuctionExpiredException();
			}

			if (!auction.checkPriceValidity(price)) {
				throw new LowPriceException();
			}

			return auction.addBid(new Bid(price, this));
		}	
	}

	/*
	 * Login method is used to login the user, it returns the user object 
	 * if the credentials are valid
	 */
	public static User login(String username, String password) throws EmptyUsernameException, EmptyPasswordException,
			ShortPasswordException, UnexistingUserException, WrongPasswordException {
		if (username == null || username.equals("")) {
			throw new EmptyUsernameException();
		}
		if (password == null || password.equals("")) {
			throw new EmptyPasswordException();
		}
		if (password.length() < 6) {
			throw new ShortPasswordException();
		}

		for (User user : users) {
			if (user.getUsername().equals(username)) {
				if (user.getPassword().equals(password)) {
					return user;
				} else {
					throw new WrongPasswordException();
				}
			}
		}

		throw new UnexistingUserException();
	}
/*
 * Search auction return a list of auctions that match the arguments.
 * The arguments are used to filter the whole list of auctions.
 * When an argument has to match all the auctions, it has to be set to a special value.
 * These special values are: categoryId (-1), description (""), minPrice (-1), maxPrice (-1), startDate (0), endDate (0) 
 */
	public ArrayList<Auction> searchAuction(int categoryId, String description, double minPrice, double maxPrice,
			long startDate, long endDate) throws InvalidCategoryException, LongDescriptionException,
			MaxPriceSmallerThanMinPriceException, EndDateBeforeStartDateException {
		ArrayList<Auction> matchingAuctions = new ArrayList<Auction>();

		if (Category.getCategories().length <= categoryId) {
			throw new InvalidCategoryException();
		}

		if (description.length() > 200) {
			throw new LongDescriptionException();
		}

		if (minPrice > maxPrice && maxPrice >= 0 && minPrice >= 0) {
			throw new MaxPriceSmallerThanMinPriceException();
		}

		if (startDate > endDate && endDate > 0 && startDate > 0 || (endDate < System.currentTimeMillis() && endDate != 0)) {
			throw new EndDateBeforeStartDateException();
		}

		for (int i = 0; i < Auction.getAuctions().size(); i++) {
			Auction auction = Auction.getAuctions().get(i);

			if ((auction.getItem().getCategory() == categoryId || categoryId == -1)
					&& (description == null || description.equals("")
							|| auction.getItem().getDescription().contains(description))
					&& (auction.getNextPrice() >= minPrice) && (maxPrice == -1 || auction.getNextPrice() <= maxPrice)
					&& (endDate == 0 || auction.getEndTime() <= endDate)
					&& (startDate == 0 || auction.getStartTime() >= startDate)) {
				matchingAuctions.add(auction);
			}
		}
		return matchingAuctions;
	}

	public ArrayList<Auction> getFollowedAuctions() {
		return followedAuctions;
	}
	
	public long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public ArrayList<Item> getOwnedItems() {
		return ownedItems;
	}

	public void setOwnedItems(ArrayList<Item> ownedItems) {
		this.ownedItems = ownedItems;
	}

	public void setFollowedAuctions(ArrayList<Auction> followedAuctions) {
		this.followedAuctions = followedAuctions;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public static ArrayList<User> getUsers(){
		return users;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass().equals(User.class)) {
			if (((User) obj).getUsername().equals(this.getUsername())
					&& ((User) obj).getPassword().equals(this.getPassword())) {
				return true;
			}
		}
		return false;
	}

}
