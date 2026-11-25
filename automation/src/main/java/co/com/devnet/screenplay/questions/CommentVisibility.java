package co.com.devnet.screenplay.questions;

import java.util.Objects;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;

public class CommentVisibility implements Question<Boolean> {
    private final String comment;

    private CommentVisibility(String comment) {
        this.comment = comment;
    }

    public static CommentVisibility containing(String comment) {
        return new CommentVisibility(comment);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        return BrowseTheWeb.as(actor).getDriver()
                .findElements(By.cssSelector(".comment .rich-text"))
                .stream()
                .map(element -> element.getText().trim())
                .anyMatch(text -> Objects.equals(text, comment));
    }
}
