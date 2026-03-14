package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HorizontalSliderPage {
    WebDriver driver;

    @FindBy(css = "div.example h3")
    WebElement pageTitle;

    @FindBy(css = "input[type='range']")
    WebElement slider;

    @FindBy(id = "range")
    WebElement rangeDisplay;

    public HorizontalSliderPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getTitle() {
        return pageTitle.getText().trim();
    }

    public double getSliderStep() {
        return Double.parseDouble(slider.getAttribute("step"));
    }

    public double getDisplayedValue() {
        return Double.parseDouble(rangeDisplay.getText().trim());
    }

    public double getSliderAttributeValue() {
        return Double.parseDouble(slider.getAttribute("value"));
    }

    public void focusSlider() {
        slider.click();
    }

    public void moveRight() {
        slider.sendKeys(Keys.ARROW_RIGHT);
    }

    public void moveLeft() {
        slider.sendKeys(Keys.ARROW_LEFT);
    }
}
