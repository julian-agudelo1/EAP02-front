package co.com.devnet.screenplay.tasks;

import co.com.devnet.screenplay.userinterfaces.DevNetUi;
import co.com.devnet.screenplay.interactions.ClickVisible;
import co.com.devnet.screenplay.interactions.DismissNotification;
import java.time.Duration;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RemoveProjectReaction implements Task {
    public RemoveProjectReaction() {
    }

    public static RemoveProjectReaction ownLike() {
        return new RemoveProjectReaction();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        var driver = BrowseTheWeb.as(actor).getDriver();
        actor.attemptsTo(DismissNotification.ifVisible(), ClickVisible.on(DevNetUi.REMOVE_REACTION));
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> {
                    var toast = d.findElement(By.id("toast"));
                    return toast != null && toast.isDisplayed() && !toast.getText().isBlank();
                });
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> d.findElements(By.className("loading-state")).isEmpty());
        actor.attemptsTo(OpenProjectComments.section());
    }
}
