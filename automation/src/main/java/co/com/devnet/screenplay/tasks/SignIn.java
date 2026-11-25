package co.com.devnet.screenplay.tasks;

import co.com.devnet.screenplay.questions.FlashMessage;
import co.com.devnet.screenplay.interactions.ClickVisible;
import co.com.devnet.screenplay.interactions.DismissNotification;
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

public class SignIn implements Task {
    private final String email;
    private final String password;

    private SignIn(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static SignIn with(String email, String password) {
        return new SignIn(email, password);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                OpenDevNet.at("#/entrar"),
                WaitUntil.the(DevNetUi.LOGIN_FORM, isVisible()).forNoMoreThan(20).seconds(),
                Enter.theValue(email).into(DevNetUi.EMAIL),
                Enter.theValue(password).into(DevNetUi.PASSWORD),
                DismissNotification.ifVisible(),
                ClickVisible.on(DevNetUi.LOGIN_SUBMIT)
        );
        FlashMessage.text().answeredBy(actor);
        new WebDriverWait(BrowseTheWeb.as(actor).getDriver(), Duration.ofSeconds(20))
                .until(driver -> !driver.findElements(By.cssSelector("#session-area .session-name")).isEmpty()
                        || !driver.findElements(By.id("login-form")).isEmpty());
    }
}
