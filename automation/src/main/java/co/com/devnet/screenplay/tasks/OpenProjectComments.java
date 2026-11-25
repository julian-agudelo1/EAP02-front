package co.com.devnet.screenplay.tasks;

import co.com.devnet.screenplay.userinterfaces.DevNetUi;
import co.com.devnet.screenplay.interactions.ClickVisible;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class OpenProjectComments implements Task {
    private OpenProjectComments() {
    }

    public static OpenProjectComments section() {
        return new OpenProjectComments();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                WaitUntil.the(DevNetUi.FIRST_PROJECT_COMMENTS, isVisible()).forNoMoreThan(20).seconds(),
                ClickVisible.on(DevNetUi.FIRST_PROJECT_COMMENTS),
                WaitUntil.the(DevNetUi.LIKE, isVisible()).forNoMoreThan(20).seconds()
        );
    }
}
