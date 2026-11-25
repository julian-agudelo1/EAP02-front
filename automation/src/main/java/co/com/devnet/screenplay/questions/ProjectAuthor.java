package co.com.devnet.screenplay.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;

public class ProjectAuthor implements Question<String> {
    private ProjectAuthor() {
    }

    public static ProjectAuthor handle() {
        return new ProjectAuthor();
    }

    @Override
    public String answeredBy(Actor actor) {
        return BrowseTheWeb.as(actor).getDriver()
                .findElement(By.cssSelector("article.card a[href^='#/perfiles/']"))
                .getText()
                .trim();
    }
}
