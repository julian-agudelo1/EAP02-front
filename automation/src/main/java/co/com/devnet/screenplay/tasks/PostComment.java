package co.com.devnet.screenplay.tasks;

import co.com.devnet.screenplay.interactions.SubmitWithoutClientValidation;
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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class PostComment implements Task {
    private final String content;
    private final boolean bypassBrowserValidation;

    private PostComment(String content, boolean bypassBrowserValidation) {
        this.content = content;
        this.bypassBrowserValidation = bypassBrowserValidation;
    }

    public static PostComment with(String content) {
        return new PostComment(content, false);
    }

    public static PostComment submittingToServer(String content) {
        return new PostComment(content, true);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                WaitUntil.the(DevNetUi.COMMENT_FORM, isVisible()).forNoMoreThan(20).seconds(),
                Enter.theValue(content).into(DevNetUi.COMMENT_BODY),
                DismissNotification.ifVisible()
        );
        if (bypassBrowserValidation) {
            actor.attemptsTo(SubmitWithoutClientValidation.on(DevNetUi.COMMENT_FORM));
            FlashMessage.text().answeredBy(actor);
            return;
        }
        actor.attemptsTo(ClickVisible.on(DevNetUi.COMMENT_SUBMIT));
        FlashMessage.text().answeredBy(actor);
    }
}
