package co.com.devnet.screenplay.tasks;

import co.com.devnet.screenplay.interactions.SubmitWithoutClientValidation;
import co.com.devnet.screenplay.questions.FlashMessage;
import co.com.devnet.screenplay.userinterfaces.DevNetUi;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Enter;
import co.com.devnet.screenplay.interactions.ClickVisible;
import co.com.devnet.screenplay.interactions.DismissNotification;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class RegisterAccount implements Task {
    private final String username;
    private final String email;
    private final String password;
    private final boolean bypassBrowserValidation;

    private RegisterAccount(String username, String email, String password, boolean bypassBrowserValidation) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.bypassBrowserValidation = bypassBrowserValidation;
    }

    public static RegisterAccount with(String username, String email, String password) {
        return new RegisterAccount(username, email, password, false);
    }

    public static RegisterAccount submittingToServer(String username, String email, String password) {
        return new RegisterAccount(username, email, password, true);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                OpenDevNet.at("#/registro"),
                WaitUntil.the(DevNetUi.REGISTRATION_FORM, isVisible()).forNoMoreThan(20).seconds(),
                Enter.theValue(username).into(DevNetUi.REGISTRATION_NAME),
                Enter.theValue(email).into(DevNetUi.EMAIL),
                Enter.theValue(password).into(DevNetUi.PASSWORD),
                DismissNotification.ifVisible()
        );
        if (bypassBrowserValidation) {
            actor.attemptsTo(SubmitWithoutClientValidation.on(DevNetUi.REGISTRATION_FORM));
        } else {
            actor.attemptsTo(ClickVisible.on(DevNetUi.REGISTRATION_SUBMIT));
        }
        FlashMessage.text().answeredBy(actor);
    }
}
