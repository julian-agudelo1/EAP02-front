package co.com.devnet.screenplay.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;

public class AcceptConfirmation implements Interaction {
    private AcceptConfirmation() {
    }

    public static AcceptConfirmation shownByTheBrowser() {
        return new AcceptConfirmation();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        BrowseTheWeb.as(actor).getDriver().switchTo().alert().accept();
    }
}
