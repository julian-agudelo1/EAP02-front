package co.com.devnet.screenplay.tasks;

import co.com.devnet.screenplay.userinterfaces.DevNetUi;
import co.com.devnet.screenplay.interactions.ClickVisible;
import co.com.devnet.screenplay.interactions.DismissNotification;
import java.time.Duration;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class PublishDiscussion implements Task {
    private final String title;
    private final String body;

    private PublishDiscussion(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public static PublishDiscussion with(String title, String body) {
        return new PublishDiscussion(title, body);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                OpenDevNet.at("#/crear/discusion"),
                WaitUntil.the(DevNetUi.DISCUSSION_FORM, isVisible()).forNoMoreThan(20).seconds(),
                ClickVisible.on(DevNetUi.FIRST_TECHNOLOGY),
                Enter.theValue(title).into(DevNetUi.DISCUSSION_TITLE),
                Enter.theValue(body).into(DevNetUi.DISCUSSION_BODY),
                DismissNotification.ifVisible(),
                ClickVisible.on(DevNetUi.DISCUSSION_SUBMIT)
        );
        new WebDriverWait(BrowseTheWeb.as(actor).getDriver(), Duration.ofSeconds(20))
                .until(driver -> driver.findElement(By.id("contenido")).getText().contains(title));
    }
}
