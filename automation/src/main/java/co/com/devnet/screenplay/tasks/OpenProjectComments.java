package co.com.devnet.screenplay.tasks;

import co.com.devnet.screenplay.userinterfaces.DevNetUi;
import co.com.devnet.screenplay.interactions.ClickVisible;
import java.time.Duration;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class OpenProjectComments implements Task {
    public OpenProjectComments() {
    }

    public static OpenProjectComments section() {
        return new OpenProjectComments();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        var driver = BrowseTheWeb.as(actor).getDriver();
        var extras = driver.findElements(By.cssSelector("[id^='project-extra-'] .interaction-row"));
        if (!extras.isEmpty() && extras.get(0).isDisplayed()) {
            return;
        }
        actor.attemptsTo(
                WaitUntil.the(DevNetUi.FIRST_PROJECT_COMMENTS, isVisible()).forNoMoreThan(20).seconds(),
                ClickVisible.on(DevNetUi.FIRST_PROJECT_COMMENTS)
        );
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(d -> {
                    var list = d.findElements(By.cssSelector("[id^='project-extra-'] .interaction-row"));
                    return !list.isEmpty() && list.get(0).isDisplayed();
                });
    }
}
