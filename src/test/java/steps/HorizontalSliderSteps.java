package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.HorizontalSliderPage;
import java.util.concurrent.TimeUnit;

public class HorizontalSliderSteps {
	private static final double EPSILON = 0.0001;

	private WebDriver driver;
	private HorizontalSliderPage sliderPage;
	private double initialValue;
	private double valueAfterRight;
	private double valueAfterLeft;

	@Before
	public void setUp() {
		try {
			System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless");
			options.addArguments("--disable-gpu");
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");
			options.addArguments("--remote-allow-origins=*");
			driver = new ChromeDriver(options);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to initialize ChromeDriver", e);
		}
	}

	@Given("I am on {string}")
	public void i_am_on(String url) {
		driver.get(url);
		sliderPage = new HorizontalSliderPage(driver);
		if (!"Horizontal Slider".equals(sliderPage.getTitle())) {
			throw new AssertionError("Unexpected page title: " + sliderPage.getTitle());
		}
		initialValue = sliderPage.getDisplayedValue();
	}

	@When("I focus on the slider")
	public void i_focus_on_the_slider() {
		sliderPage.focusSlider();
		initialValue = sliderPage.getDisplayedValue();
	}

	@When("I move the slider right")
	public void i_move_the_slider_right() {
		sliderPage.moveRight();
		valueAfterRight = sliderPage.getDisplayedValue();
	}

	@When("I move the slider left")
	public void i_move_the_slider_left() {
		sliderPage.moveLeft();
		valueAfterLeft = sliderPage.getDisplayedValue();
	}

	@Then("I should see the slider value updated on the right")
	public void i_should_see_the_slider_value_updated_on_the_right() {
		double step = sliderPage.getSliderStep();
		double expectedAfterRight = initialValue + step;
		double expectedAfterLeft = expectedAfterRight - step;

		if (Math.abs(valueAfterRight - expectedAfterRight) > EPSILON) {
			throw new AssertionError(
				"Unexpected value after moving right. Expected " + expectedAfterRight + " but was " + valueAfterRight
			);
		}

		if (Math.abs(valueAfterLeft - expectedAfterLeft) > EPSILON) {
			throw new AssertionError(
				"Unexpected value after moving left. Expected " + expectedAfterLeft + " but was " + valueAfterLeft
			);
		}

		double sliderAttributeValue = sliderPage.getSliderAttributeValue();
		if (Math.abs(sliderAttributeValue - valueAfterLeft) > EPSILON) {
			throw new AssertionError(
				"Slider input value and displayed value are out of sync. input=" + sliderAttributeValue + ", display=" + valueAfterLeft
			);
		}
	}

	@After
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
