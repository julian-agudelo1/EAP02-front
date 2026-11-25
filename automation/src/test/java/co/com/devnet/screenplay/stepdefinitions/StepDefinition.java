package co.com.devnet.screenplay.stepdefinitions;

import co.com.devnet.screenplay.questions.CommentAuthor;
import co.com.devnet.screenplay.questions.CommentVisibility;
import co.com.devnet.screenplay.questions.FlashMessage;
import co.com.devnet.screenplay.questions.ProjectAuthor;
import co.com.devnet.screenplay.questions.ReactionCount;
import co.com.devnet.screenplay.interactions.ClickVisible;
import co.com.devnet.screenplay.interactions.DismissNotification;
import co.com.devnet.screenplay.tasks.DeleteVisibleComment;
import co.com.devnet.screenplay.tasks.OpenDevNet;
import co.com.devnet.screenplay.tasks.OpenProjectComments;
import co.com.devnet.screenplay.tasks.PostComment;
import co.com.devnet.screenplay.tasks.PublishDiscussion;
import co.com.devnet.screenplay.tasks.PublishProject;
import co.com.devnet.screenplay.tasks.ReactToProject;
import co.com.devnet.screenplay.tasks.RegisterAccount;
import co.com.devnet.screenplay.tasks.RemoveProjectReaction;
import co.com.devnet.screenplay.tasks.SignIn;
import co.com.devnet.screenplay.userinterfaces.DevNetUi;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StepDefinition {
    private static final Pattern TRACE_REFERENCE = Pattern.compile("Referencia\\s+([A-Za-z0-9-]+)");
    private TestData data;

    @Before
    public void configureActor() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("DevNet member");
        data = new TestData();
    }

    @Given("the visitor opens the registration page")
    public void visitorOpensRegistrationPage() {
        actor().attemptsTo(OpenDevNet.at("#/registro"));
    }

    @When("the visitor registers with a unique email and a strong password")
    public void visitorRegistersWithValidDetails() {
        registerMember();
    }

    @Then("the account-created confirmation is shown")
    public void accountCreatedConfirmationIsShown() {
        String message = FlashMessage.text().answeredBy(actor());
        Assert.assertTrue("Expected a visible account-created confirmation.", message.toLowerCase().contains("cuenta"));
    }

    @And("the visitor is asked to sign in")
    public void visitorIsAskedToSignIn() {
        Assert.assertTrue("Registration should return the visitor to sign-in.", currentUrl().endsWith("#/entrar"));
    }

    @Given("a member account already exists")
    public void memberAccountAlreadyExists() {
        registerMember();
    }

    @When("the visitor registers again with the same email")
    public void visitorRegistersAgainWithSameEmail() {
        actor().attemptsTo(RegisterAccount.with(data.username + "again", data.email, data.password));
    }

    @Then("the registration is rejected with a clear message")
    public void registrationIsRejectedClearly() {
        String message = FlashMessage.text().answeredBy(actor()).toLowerCase();
        Assert.assertTrue("Expected a clear duplicate-account error.", containsAny(message, "correo", "email", "registr", "cuenta"));
        Assert.assertFalse("A rejected registration must not create a signed-in session.", isSignedIn());
    }

    @When("the visitor submits an invalid email to the server")
    public void visitorSubmitsInvalidEmailToServer() {
        actor().attemptsTo(RegisterAccount.submittingToServer(data.username, "not-an-email", data.password));
    }

    @Then("the server rejects the registration")
    public void serverRejectsRegistration() {
        assertErrorNotification("Expected the server to reject an invalid email.");
    }

    @When("the visitor submits a password that violates the password policy")
    public void visitorSubmitsWeakPassword() {
        actor().attemptsTo(RegisterAccount.submittingToServer(data.username, data.email, "lowercaseonly"));
    }

    @Then("the error explains the password requirements")
    public void errorExplainsPasswordRequirements() {
        String message = FlashMessage.text().answeredBy(actor()).toLowerCase();
        Assert.assertTrue("The server should explain the password requirements. Received: " + message,
                containsAny(message, "contraseña", "password", "carácter", "caracter", "regla", "requisito"));
    }

    @When("the member signs in with valid credentials")
    public void memberSignsInWithValidCredentials() {
        signInAsMember();
    }

    @Then("the member's signed-in session is visible")
    public void signedInSessionIsVisible() {
        String actualUsername = driver().findElements(By.cssSelector("#session-area .session-name")).stream()
                .findFirst()
                .map(element -> element.getText().trim())
                .orElse("");
        Assert.assertEquals("Signed-in session was not visible. Last notification: "
                        + FlashMessage.text().answeredBy(actor()),
                "@" + data.username, actualUsername);
    }

    @And("the projects feed is displayed")
    public void projectsFeedIsDisplayed() {
        Assert.assertTrue("A successful sign-in should open the projects feed.",
                currentUrl().endsWith("#/proyectos") && pageText().contains("Proyectos"));
    }

    @When("the member signs in with an incorrect password")
    public void memberSignsInWithIncorrectPassword() {
        actor().attemptsTo(SignIn.with(data.email, data.password + "-incorrecta"));
        data.originalLoginError = FlashMessage.text().answeredBy(actor());
    }

    @And("a visitor signs in with a nonexistent email")
    public void visitorSignsInWithNonexistentEmail() {
        actor().attemptsTo(SignIn.with("unknown." + data.email, data.password + "-incorrecta"));
    }

    @Then("both attempts show the same generic credentials error")
    public void bothAttemptsShowSameGenericError() {
        String latestError = FlashMessage.text().answeredBy(actor());
        Assert.assertEquals("Unknown users and incorrect passwords must receive the same message.",
                data.originalLoginError, latestError);
        String normalized = latestError.toLowerCase();
        Assert.assertTrue("Expected a generic invalid-credentials message. Received: " + latestError,
                containsAny(normalized, "credencial", "credentials", "inválid", "invalid"));
        Assert.assertFalse("The message must not identify the failed field.",
                containsAny(normalized, "correo", "email", "contraseña", "password"));
    }

    @Given("a failed sign-in attempt has a trace reference")
    public void failedSignInAttemptHasTraceReference() {
        actor().attemptsTo(SignIn.with("unknown." + data.email, data.password));
        Matcher matcher = TRACE_REFERENCE.matcher(FlashMessage.text().answeredBy(actor()));
        Assert.assertTrue("The API error should expose a trace reference for the failed attempt.", matcher.find());
        data.traceId = matcher.group(1);
    }

    @When("an administrator opens the audit page")
    public void administratorOpensAuditPage() {
        actor().attemptsTo(
                SignIn.with(TestData.adminEmail(), TestData.adminPassword()),
                OpenDevNet.at("#/auditoria")
        );
        waitForText("Auditoría");
    }

    @Then("the failed attempt is present in the audit records")
    public void failedAttemptIsPresentInAuditRecords() {
        new WebDriverWait(driver(), Duration.ofSeconds(20))
                .until(browser -> browser.getPageSource().contains(data.traceId));
        Assert.assertTrue(pageText().contains(data.traceId));
    }

    @Given("a signed-in member")
    public void signedInMember() {
        signInAsNewMember();
    }

    @When("the member publishes a project with a title, description, and technology")
    public void memberPublishesValidProject() {
        publishProject();
    }

    @Then("the project appears in the member's project list")
    public void projectAppearsInMemberList() {
        Assert.assertTrue("The newly published project should be visible on the member's profile.",
                pageText().contains(data.projectTitle));
    }

    @And("the project is attributed to the signed-in member")
    public void projectIsAttributedToSignedInMember() {
        Assert.assertEquals("@" + data.username, ProjectAuthor.handle().answeredBy(actor()));
    }

    @When("the member submits a project with a missing {string} to the server")
    public void memberSubmitsProjectMissingRequiredField(String field) {
        String title = "title".equals(field) ? "" : data.projectTitle;
        String description = "description".equals(field) ? "" : data.projectDescription;
        actor().attemptsTo(PublishProject.submittingToServer(title, description));
    }

    @Then("the project is rejected with a validation message")
    public void projectIsRejectedWithValidationMessage() {
        assertErrorNotification("Expected server-side project validation.");
    }

    @Given("a signed-in member viewing a project they own")
    public void signedInMemberViewingOwnedProject() {
        signInAsNewMember();
        publishProject();
        actor().attemptsTo(OpenProjectComments.section());
        data.originalReactionCount = ReactionCount.onTheVisibleProject().answeredBy(actor());
    }

    @Given("a signed-in member viewing a discussion they created")
    public void signedInMemberViewingOwnDiscussion() {
        signInAsNewMember();
        actor().attemptsTo(PublishDiscussion.with(data.discussionTitle, data.discussionBody));
    }

    @When("the member publishes a valid comment")
    public void memberPublishesValidComment() {
        actor().attemptsTo(PostComment.with(data.comment));
    }

    @Then("the comment is displayed with its author's identity")
    public void commentIsDisplayedWithAuthorIdentity() {
        if (currentUrl().contains("/perfiles/")) {
            actor().attemptsTo(OpenProjectComments.section());
        }
        Assert.assertTrue("The posted comment should be visible.", CommentVisibility.containing(data.comment).answeredBy(actor()));
        Assert.assertEquals("@" + data.username, CommentAuthor.handle().answeredBy(actor()));
    }

    @When("the member submits an empty comment to the server")
    public void memberSubmitsEmptyCommentToServer() {
        actor().attemptsTo(PostComment.submittingToServer(""));
    }

    @Then("the comment is rejected with a validation message")
    public void commentIsRejectedWithValidationMessage() {
        assertErrorNotification("Expected server-side validation to reject an empty comment.");
    }

    @Given("a signed-in member viewing a project with their comment")
    public void signedInMemberViewingProjectWithOwnComment() {
        signedInMemberViewingOwnedProject();
        actor().attemptsTo(PostComment.with(data.comment), OpenProjectComments.section());
    }

    @When("the author deletes that comment and confirms")
    public void authorDeletesOwnCommentAndConfirms() {
        actor().attemptsTo(DeleteVisibleComment.withContent(data.comment));
    }

    @Then("the comment is no longer displayed")
    public void commentIsNoLongerDisplayed() {
        if (!currentUrl().contains("/discusiones/")) {
            actor().attemptsTo(OpenProjectComments.section());
        }
        Assert.assertFalse("The deleted comment should no longer be present.",
                CommentVisibility.containing(data.comment).answeredBy(actor()));
    }

    @Given("a member's project has a comment by its author")
    public void membersProjectHasCommentByAuthor() {
        signInAsNewMember();
        publishProject();
        data.authorProfileHash = authorProfileHash();
        actor().attemptsTo(OpenProjectComments.section(), PostComment.with(data.comment), OpenProjectComments.section());
    }

    @When("an administrator deletes that comment and confirms")
    public void administratorDeletesAnotherMembersComment() {
        actor().attemptsTo(
                DismissNotification.ifVisible(),
                ClickVisible.on(DevNetUi.LOGOUT),
                SignIn.with(TestData.adminEmail(), TestData.adminPassword()),
                OpenDevNet.at(data.authorProfileHash),
                OpenProjectComments.section(),
                DeleteVisibleComment.withContent(data.comment)
        );
    }

    @When("the member adds a like")
    public void memberAddsLike() {
        actor().attemptsTo(ReactToProject.withLike());
    }

    @Then("the reaction count increases by one")
    public void reactionCountIncreasesByOne() {
        Assert.assertEquals(data.originalReactionCount + 1,
                (int) ReactionCount.onTheVisibleProject().answeredBy(actor()));
    }

    @When("the member removes the like")
    public void memberRemovesLike() {
        actor().attemptsTo(RemoveProjectReaction.ownLike());
    }

    @Then("the reaction count returns to its original value")
    public void reactionCountReturnsToOriginalValue() {
        Assert.assertEquals(data.originalReactionCount,
                (int) ReactionCount.onTheVisibleProject().answeredBy(actor()));
    }

    @When("the member adds the same like twice")
    public void memberAddsSameLikeTwice() {
        actor().attemptsTo(ReactToProject.withLike(), ReactToProject.withLike());
    }

    @Then("only one reaction from that member is counted")
    public void onlyOneReactionIsCounted() {
        Assert.assertEquals(data.originalReactionCount + 1,
                (int) ReactionCount.onTheVisibleProject().answeredBy(actor()));
    }

    private Actor actor() {
        return OnStage.theActorInTheSpotlight();
    }

    private void registerMember() {
        actor().attemptsTo(RegisterAccount.with(data.username, data.email, data.password));
    }

    private void signInAsMember() {
        actor().attemptsTo(SignIn.with(data.email, data.password));
    }

    private void signInAsNewMember() {
        registerMember();
        signInAsMember();
    }

    private void publishProject() {
        actor().attemptsTo(PublishProject.with(data.projectTitle, data.projectDescription));
    }

    private String currentUrl() {
        return driver().getCurrentUrl();
    }

    private String pageText() {
        return driver().findElement(By.id("contenido")).getText();
    }

    private boolean isSignedIn() {
        return !driver().findElements(By.cssSelector("#session-area .session-name")).isEmpty();
    }

    private String authorProfileHash() {
        String href = driver().findElement(By.cssSelector("article.card a[href^='#/perfiles/']"))
                .getAttribute("href");
        return href.substring(href.indexOf('#'));
    }

    private WebDriver driver() {
        return BrowseTheWeb.as(actor()).getDriver();
    }

    private void waitForText(String expected) {
        new WebDriverWait(driver(), Duration.ofSeconds(20))
                .until(browser -> browser.findElement(By.id("contenido")).getText().contains(expected));
    }

    private void assertErrorNotification(String message) {
        String notification = FlashMessage.text().answeredBy(actor()).toLowerCase();
        Assert.assertTrue(message + " Notification was: " + notification,
                driver().findElement(By.id("toast")).getAttribute("class").contains("error"));
        Assert.assertTrue(message + " Notification was: " + notification,
                containsAny(notification, "error", "inválid", "invalid", "requer", "obligator",
                        "debe", "no puede", "mínim", "minim", "correo", "email", "comentario", "título", "descripción"));
        Assert.assertFalse("The rejected operation must not report success.",
                containsAny(notification, "publicado", "creada", "creado", "bienvenido"));
    }

    private boolean containsAny(String text, String... fragments) {
        for (String fragment : fragments) {
            if (text.contains(fragment)) {
                return true;
            }
        }
        return false;
    }
}
