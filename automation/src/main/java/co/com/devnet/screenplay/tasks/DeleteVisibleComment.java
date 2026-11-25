package co.com.devnet.screenplay.tasks;

import java.time.Duration;
import co.com.devnet.screenplay.interactions.DismissNotification;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DeleteVisibleComment implements Task {
    private final String content;

    private DeleteVisibleComment(String content) {
        this.content = content;
    }

    public static DeleteVisibleComment withContent(String content) {
        return new DeleteVisibleComment(content);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        var driver = BrowseTheWeb.as(actor).getDriver();
        actor.attemptsTo(DismissNotification.ifVisible());
        WebElement comment = driver.findElements(By.cssSelector(".comment"))
                .stream()
                .filter(element -> element.getText().contains(content))
                .findFirst()
                .orElseThrow(() -> new AssertionError("The target comment is not visible."));
        comment.findElement(By.cssSelector("button[data-action='delete-comment']")).click();
        driver.switchTo().alert().accept();
        new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.stalenessOf(comment));
    }
}
