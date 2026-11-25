package co.com.devnet.screenplay.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;

public class ReactionCount implements Question<Integer> {
    private ReactionCount() {
    }

    public static ReactionCount onTheVisibleProject() {
        return new ReactionCount();
    }

    @Override
    public Integer answeredBy(Actor actor) {
        String text = BrowseTheWeb.as(actor).getDriver()
                .findElement(By.cssSelector("[id^='project-extra-'] .interaction-row .pill"))
                .getText();
        return Integer.parseInt(text.replaceAll("[^0-9]", ""));
    }
}
