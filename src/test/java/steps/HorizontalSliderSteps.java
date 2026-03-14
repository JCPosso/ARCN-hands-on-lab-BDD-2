package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class HorizontalSliderSteps {
	private static final double EPSILON = 0.0001;

	private WebDriver driver;
	private WebElement slider;
	private double initialValue;
	private double valueAfterRight;
	private double valueAfterLeft;
	private double sliderStep;

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
		String title = driver.findElement(By.cssSelector("div.example h3")).getText().trim();
		if (!"Horizontal Slider".equals(title)) {
			throw new AssertionError("Unexpected page title: " + title);
		}

		slider = driver.findElement(By.cssSelector("input[type='range']"));
		sliderStep = Double.parseDouble(slider.getAttribute("step"));
		initialValue = readSliderValue();
	}

	@When("I focus on the slider")
	public void i_focus_on_the_slider() {
		slider.click();
	}

	@When("I move the slider right")
	public void i_move_the_slider_right() {
		slider.sendKeys(Keys.ARROW_RIGHT);
		valueAfterRight = readSliderValue();
	}

	@When("I move the slider left")
	public void i_move_the_slider_left() {
		slider.sendKeys(Keys.ARROW_LEFT);
		valueAfterLeft = readSliderValue();
	}

	@Then("I should see the slider value updated on the right")
	public void i_should_see_the_slider_value_updated_on_the_right() {
		double expectedAfterRight = initialValue + sliderStep;
		double expectedAfterLeft = expectedAfterRight - sliderStep;

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

		double sliderAttributeValue = Double.parseDouble(slider.getAttribute("value"));
		if (Math.abs(sliderAttributeValue - valueAfterLeft) > EPSILON) {
			throw new AssertionError(
				"Slider input value and displayed value are out of sync. input=" + sliderAttributeValue + ", display=" + valueAfterLeft
			);
		}
	}

	private double readSliderValue() {
		String valueText = driver.findElement(By.id("range")).getText().trim();
		return Double.parseDouble(valueText);
	}

	@After
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
