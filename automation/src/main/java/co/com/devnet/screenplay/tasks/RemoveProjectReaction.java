package co.com.devnet.screenplay.tasks;

import co.com.devnet.screenplay.userinterfaces.DevNetUi;
import co.com.devnet.screenplay.interactions.ClickVisible;
import co.com.devnet.screenplay.interactions.DismissNotification;
import java.time.Duration;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RemoveProjectReaction implements Task {
    private RemoveProjectReaction() {
    }

    public static RemoveProjectReaction ownLike() {
        return new RemoveProjectReaction();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        WebElement oldButton = DevNetUi.REMOVE_REACTION.resolveFor(actor);
        actor.attemptsTo(DismissNotification.ifVisible(), ClickVisible.on(DevNetUi.REMOVE_REACTION));
        new WebDriverWait(BrowseTheWeb.as(actor).getDriver(), Duration.ofSeconds(20))
                .until(ExpectedConditions.stalenessOf(oldButton));
        actor.attemptsTo(OpenProjectComments.section());
    }
}
