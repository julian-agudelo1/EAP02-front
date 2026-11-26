package co.com.devnet.screenplay.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Open;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class OpenDevNet implements Task {
    private final String route;

    private OpenDevNet(String route) {
        this.route = route;
    }

    public static OpenDevNet at(String route) {
        return new OpenDevNet(route);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        String currentUrl = driver.getCurrentUrl();
        String baseUrl = TestUrls.frontend().replaceAll("/+$", "");
        if (currentUrl != null && currentUrl.startsWith(baseUrl)) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                    "var r = arguments[0]; if (location.hash !== r) { location.hash = r; } window.dispatchEvent(new HashChangeEvent('hashchange'));",
                    route
            );
        } else {
            actor.attemptsTo(Open.url(TestUrls.frontend() + route));
        }
    }
}
