package co.com.devnet.screenplay.tasks;

import co.com.devnet.screenplay.interactions.SubmitWithoutClientValidation;
import co.com.devnet.screenplay.interactions.ClickVisible;
import co.com.devnet.screenplay.interactions.DismissNotification;
import co.com.devnet.screenplay.questions.FlashMessage;
import co.com.devnet.screenplay.userinterfaces.DevNetUi;
import java.time.Duration;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class PublishProject implements Task {
    private final String title;
    private final String description;
    private final boolean bypassBrowserValidation;

    private PublishProject(String title, String description, boolean bypassBrowserValidation) {
        this.title = title;
        this.description = description;
        this.bypassBrowserValidation = bypassBrowserValidation;
    }

    public static PublishProject with(String title, String description) {
        return new PublishProject(title, description, false);
    }

    public static PublishProject submittingToServer(String title, String description) {
        return new PublishProject(title, description, true);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                OpenDevNet.at("#/crear/proyecto"),
                WaitUntil.the(DevNetUi.PROJECT_FORM, isVisible()).forNoMoreThan(20).seconds(),
                ClickVisible.on(DevNetUi.FIRST_TECHNOLOGY),
                Enter.theValue(title).into(DevNetUi.PROJECT_TITLE),
                Enter.theValue(description).into(DevNetUi.PROJECT_DESCRIPTION),
                DismissNotification.ifVisible()
        );
        if (bypassBrowserValidation) {
            actor.attemptsTo(SubmitWithoutClientValidation.on(DevNetUi.PROJECT_FORM));
            FlashMessage.text().answeredBy(actor);
            return;
        }
        actor.attemptsTo(ClickVisible.on(DevNetUi.PROJECT_SUBMIT));
        new WebDriverWait(BrowseTheWeb.as(actor).getDriver(), Duration.ofSeconds(20))
                .until(driver -> driver.findElements(By.cssSelector("article.card h2"))
                        .stream()
                        .anyMatch(element -> element.getText().equals(title)));
    }
}
