package co.com.devnet.screenplay.interactions;

import java.time.Duration;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ClickVisible implements Interaction {
    private final Target target;

    private ClickVisible(Target target) {
        this.target = target;
    }

    public static ClickVisible on(Target target) {
        return new ClickVisible(target);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        var driver = BrowseTheWeb.as(actor).getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.ignoring(StaleElementReferenceException.class).until(browser -> {
            WebElement element = target.resolveFor(actor);
            ((JavascriptExecutor) browser).executeScript(
                    "arguments[0].scrollIntoView({ block: 'center', inline: 'nearest' });",
                    element
            );
            return element.isDisplayed() && element.isEnabled() ? element : null;
        });
        WebElement element = target.resolveFor(actor);
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }
}
