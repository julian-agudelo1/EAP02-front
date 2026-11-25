package co.com.devnet.screenplay.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;

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
        actor.attemptsTo(Open.url(TestUrls.frontend() + route));
    }
}
