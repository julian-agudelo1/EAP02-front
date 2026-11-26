package co.com.devnet.screenplay.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "automation/src/test/resources/features/engagement/comments.feature",
        glue = "co.com.devnet.screenplay.stepdefinitions",
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class CommentsRunner {
}
