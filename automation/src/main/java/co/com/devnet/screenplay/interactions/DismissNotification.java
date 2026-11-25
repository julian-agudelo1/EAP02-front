package co.com.devnet.screenplay.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.JavascriptExecutor;

public class DismissNotification implements Interaction {
    private final String selector;

    private DismissNotification(String selector) {
        this.selector = selector;
    }

    public static DismissNotification ifVisible() {
        return new DismissNotification("#toast");
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        ((JavascriptExecutor) BrowseTheWeb.as(actor).getDriver()).executeScript(
                "const toast = document.querySelector(arguments[0]);" +
                        "if (toast) { toast.hidden = true; toast.textContent = ''; toast.classList.remove('error'); }",
                selector
        );
    }
}
