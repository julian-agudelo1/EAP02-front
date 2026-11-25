package co.com.devnet.screenplay.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;

public class CommentAuthor implements Question<String> {
    private CommentAuthor() {
    }

    public static CommentAuthor handle() {
        return new CommentAuthor();
    }

    @Override
    public String answeredBy(Actor actor) {
        return BrowseTheWeb.as(actor).getDriver()
                .findElement(By.cssSelector(".comment .meta-row a[href^='#/perfiles/']"))
                .getText()
                .trim();
    }
}
