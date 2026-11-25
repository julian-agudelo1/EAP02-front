package co.com.devnet.screenplay.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "co.com.devnet.screenplay.stepdefinitions",
        tags = "not @pending-ui and not @requires-admin",
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class Runner {
}