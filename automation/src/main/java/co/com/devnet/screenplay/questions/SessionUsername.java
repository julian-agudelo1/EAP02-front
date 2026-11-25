package co.com.devnet.screenplay.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;

public class SessionUsername implements Question<String> {
    private SessionUsername() {
    }

    public static SessionUsername visibleAs() {
        return new SessionUsername();
    }

    @Override
    public String answeredBy(Actor actor) {
        return BrowseTheWeb.as(actor).getDriver()
                .findElement(By.cssSelector("#session-area .session-name"))
                .getText()
                .trim();
    }
}
