package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class PlaceBidUITest {

	static WebDriver driver;

	static final String username = "a9";
	static final String password = "b9";

	final String startingPrice = "1000";
	final String bidIncrement = "1000";

	private final long MINUTE = 6;
	private final long HOUR = 3600000;
	private final long DAY = 86400000;
	private final long WEEK = 604800000;

	LocalDateTime dateNow = LocalDateTime.now();

	@BeforeAll
	public static void setup() {
		WebDriverManager.chromedriver().setup();

		driver = new ChromeDriver();
		driver.get("http://localhost:8080");

		// first time only if not registered before!
		// otherwise comment out
		register();
	}

	@BeforeEach
	public void logInEveryTime() {
		logIn();
	}

	@AfterEach
	public void logOut() {
		WebElement logoutButton = driver.findElement(By.linkText("Logout"));
		logoutButton.click();

	}

	public static void register() {
		WebElement signInLink = driver.findElement(By.linkText("Sign in"));
		signInLink.click();

		WebElement signUpLink = driver.findElement(By.linkText("Sign Up"));
		signUpLink.click();

		WebElement usernameInput = driver.findElement(By.id("inputUsername"));
		usernameInput.sendKeys(username);
		WebElement passwordInput = driver.findElement(By.id("inputPassword"));
		passwordInput.sendKeys(password);

		WebElement signInButton = driver.findElement(By.tagName("button"));
		signInButton.click();
	}

	public static void logIn() {
		WebElement signInLink = driver.findElement(By.linkText("Sign in"));
		signInLink.click();

		WebElement usernameInput = driver.findElement(By.id("inputUsername"));
		usernameInput.sendKeys(username);
		WebElement passwordInput = driver.findElement(By.id("inputPassword"));
		passwordInput.sendKeys(password);

		WebElement signInButton = driver.findElement(By.tagName("button"));
		signInButton.click();
	}

	public void createAuction(String startDate, String startHour, String endDate, String endHour) {

		WebElement auctionsLink = driver.findElement(By.linkText("Auctions"));
		auctionsLink.click();

		WebElement newAuctionButton = driver.findElement(By.linkText("Create new auction"));
		newAuctionButton.sendKeys(Keys.ENTER);

		// inputStartPrice

		WebElement startPriceInput = driver.findElement(By.id("inputStartPrice"));
		startPriceInput.sendKeys(startingPrice);

		// bid increment
		WebElement bidIncrementInput = driver.findElement(By.id("inputBidIncrement"));
		bidIncrementInput.sendKeys(bidIncrement);

		// start time
		WebElement startTimeInput = driver.findElement(By.id("inputStartTime"));
		startTimeInput.sendKeys(startDate + Keys.TAB + startHour);

		// end time
		WebElement endTimeInput = driver.findElement(By.id("inputEndTime"));
		endTimeInput.sendKeys(endDate + Keys.TAB + endHour);

		// create auction
		WebElement createAuctionButton = driver.findElement(By.tagName("button"));
		createAuctionButton.click();

		List<WebElement> auctions = driver.findElements(By.linkText("See auction"));
		WebElement seeLastAuctionButton = auctions.get(auctions.size() - 1);
		seeLastAuctionButton.sendKeys(Keys.ENTER);
	}

	@ParameterizedTest
	@ValueSource(strings = { "2000.00", "3000.00", "4000.00", "10000.00" })
	void ifOpenedAuctionPlaceBid(String bid) {

		// Initialize auction
		String startDate = dateNow.minusDays(1).format(DateTimeFormatter.ofPattern("ddMMyyyy"));
		String startHour = dateNow.minusDays(1).format(DateTimeFormatter.ofPattern("HH:mm"));
		String endDate = dateNow.plusDays(10).format(DateTimeFormatter.ofPattern("ddMMyyyy"));
		String endHour = dateNow.plusDays(10).format(DateTimeFormatter.ofPattern("HH:mm"));

		createAuction(startDate, startHour, endDate, endHour);

		// place new bid
		WebElement priceInput = driver.findElement(By.id("inputPrice"));
		priceInput.sendKeys(bid);

		WebElement placeBidButton = driver.findElement(By.tagName("button"));
		placeBidButton.sendKeys(Keys.ENTER);

		WebElement winningPrice = driver.findElement(By.id("winningPrice"));
		winningPrice.getText();

		assertEquals(bid, winningPrice.getText());
	}

	@ParameterizedTest
	@ValueSource(strings = { "998.00", "500.00", "999.00", "1.00" })
	void ifInvalidBidThrowException(String bid) {
		// Initialize auction
		String startDate = dateNow.minusDays(1).format(DateTimeFormatter.ofPattern("ddMMyyyy"));
		String startHour = dateNow.minusDays(1).format(DateTimeFormatter.ofPattern("HH:mm"));
		String endDate = dateNow.plusDays(10).format(DateTimeFormatter.ofPattern("ddMMyyyy"));
		String endHour = dateNow.plusDays(10).format(DateTimeFormatter.ofPattern("HH:mm"));

		createAuction(startDate, startHour, endDate, endHour);

		WebElement priceInput = driver.findElement(By.id("inputPrice"));
		// place new bid
		priceInput.sendKeys(bid);

		WebElement placeBidButton = driver.findElement(By.tagName("button"));
		placeBidButton.sendKeys(Keys.ENTER);

		assertEquals("The price is too low", driver.findElement(By.className("alert")).getText());
	}

	@ParameterizedTest
	@ValueSource(longs = { 10, MINUTE, HOUR, DAY, WEEK })
	void ifNotOpenedAuctionThrowException(long seconds) {

		String startDate = dateNow.plusSeconds(seconds).format(DateTimeFormatter.ofPattern("ddMMyyyy"));
		String startHour = dateNow.plusSeconds(seconds).format(DateTimeFormatter.ofPattern("HH:mm"));

		String endDate = dateNow.plusSeconds(seconds * 10).format(DateTimeFormatter.ofPattern("ddMMyyyy"));
		String endHour = dateNow.plusSeconds(seconds * 10).format(DateTimeFormatter.ofPattern("HH:mm"));

		createAuction(startDate, startHour, endDate, endHour);

		List<WebElement> paragraphs = driver.findElements(By.tagName("p"));
		WebElement lastParagraph = paragraphs.get(paragraphs.size() - 1);

		assertEquals("The auction has not started yet.", lastParagraph.getText());
	}

	@ParameterizedTest
	@ValueSource(longs = {HOUR, DAY, WEEK })
	void ifAuctionExpiredThrowException1(long seconds) {

		String startDate = dateNow.minusSeconds(seconds * 10).format(DateTimeFormatter.ofPattern("ddMMyyyy"));
		String startHour = dateNow.minusSeconds(seconds * 10).format(DateTimeFormatter.ofPattern("HH:mm"));

		String endDate = dateNow.minusSeconds(seconds).format(DateTimeFormatter.ofPattern("ddMMyyyy"));
		String endHour = dateNow.minusSeconds(seconds).format(DateTimeFormatter.ofPattern("HH:mm"));

		createAuction(startDate, startHour, endDate, endHour);

		List<WebElement> paragraphs = driver.findElements(By.tagName("p"));
		WebElement lastParagraph = paragraphs.get(paragraphs.size() - 1);

		assertEquals("The auction has ended.", lastParagraph.getText());

	}

}
