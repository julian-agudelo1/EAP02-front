package co.com.devnet.screenplay.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class SubmitWithoutClientValidation implements Interaction {
    private final Target formTarget;

    private SubmitWithoutClientValidation(Target formTarget) {
        this.formTarget = formTarget;
    }

    public static SubmitWithoutClientValidation on(Target formTarget) {
        return new SubmitWithoutClientValidation(formTarget);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        WebElement form = formTarget.resolveFor(actor);
        JavascriptExecutor browser = (JavascriptExecutor) BrowseTheWeb.as(actor).getDriver();
        browser.executeScript(
                "arguments[0].querySelectorAll('input, textarea, select').forEach(field => {" +
                        "field.removeAttribute('required');" +
                        "field.removeAttribute('minlength');" +
                        "field.removeAttribute('maxlength');" +
                        "field.removeAttribute('pattern');" +
                        "if (field.type === 'email') field.type = 'text';" +
                        "});" +
                        "arguments[0].dispatchEvent(new Event('submit', { bubbles: true, cancelable: true }));",
                form
        );
    }
}
