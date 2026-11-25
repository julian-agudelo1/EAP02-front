package co.com.devnet.screenplay.questions;

import java.time.Duration;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FlashMessage implements Question<String> {
    private FlashMessage() {
    }

    public static FlashMessage text() {
        return new FlashMessage();
    }

    @Override
    public String answeredBy(Actor actor) {
        WebDriverWait wait = new WebDriverWait(BrowseTheWeb.as(actor).getDriver(), Duration.ofSeconds(20));
        WebElement toast = wait.until(driver -> {
            WebElement element = driver.findElement(By.id("toast"));
            return element.isDisplayed() && !element.getText().isBlank() ? element : null;
        });
        return toast.getText().trim();
    }
}
