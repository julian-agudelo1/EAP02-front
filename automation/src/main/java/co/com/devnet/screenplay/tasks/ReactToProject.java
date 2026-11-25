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

public class ReactToProject implements Task {
    private ReactToProject() {
    }

    public static ReactToProject withLike() {
        return new ReactToProject();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        WebElement oldLike = DevNetUi.LIKE.resolveFor(actor);
        actor.attemptsTo(DismissNotification.ifVisible(), ClickVisible.on(DevNetUi.LIKE));
        new WebDriverWait(BrowseTheWeb.as(actor).getDriver(), Duration.ofSeconds(20))
                .until(ExpectedConditions.stalenessOf(oldLike));
        actor.attemptsTo(OpenProjectComments.section());
    }
}
