package co.com.devnet.screenplay.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public final class DevNetUi {
    public static final Target REGISTRATION_FORM = Target.the("registration form").located(By.id("register-form"));
    public static final Target REGISTRATION_NAME = Target.the("username field").located(By.id("auth-name"));
    public static final Target EMAIL = Target.the("email field").located(By.id("auth-email"));
    public static final Target PASSWORD = Target.the("password field").located(By.id("auth-password"));
    public static final Target REGISTRATION_SUBMIT = Target.the("create account button")
            .located(By.cssSelector("#register-form button[type='submit']"));
    public static final Target LOGIN_FORM = Target.the("sign-in form").located(By.id("login-form"));
    public static final Target LOGIN_SUBMIT = Target.the("sign-in button")
            .located(By.cssSelector("#login-form button[type='submit']"));
    public static final Target PROJECT_FORM = Target.the("project form").located(By.id("project-form"));
    public static final Target PROJECT_TITLE = Target.the("project title").located(By.id("project-title"));
    public static final Target PROJECT_DESCRIPTION = Target.the("project description").located(By.id("project-description"));
    public static final Target FIRST_TECHNOLOGY = Target.the("first available technology")
            .located(By.cssSelector("input[name='tecnologias']"));
    public static final Target PROJECT_SUBMIT = Target.the("publish project button")
            .located(By.cssSelector("#project-form button[type='submit']"));
    public static final Target DISCUSSION_FORM = Target.the("discussion form").located(By.id("discussion-form"));
    public static final Target DISCUSSION_TITLE = Target.the("discussion title").located(By.id("discussion-title"));
    public static final Target DISCUSSION_BODY = Target.the("discussion body").located(By.id("discussion-body"));
    public static final Target DISCUSSION_SUBMIT = Target.the("publish discussion button")
            .located(By.cssSelector("#discussion-form button[type='submit']"));
    public static final Target COMMENT_FORM = Target.the("comment form")
            .located(By.cssSelector("form[data-form='comment']"));
    public static final Target COMMENT_BODY = Target.the("comment text").located(By.cssSelector("form[data-form='comment'] textarea[name='contenido']"));
    public static final Target COMMENT_SUBMIT = Target.the("publish comment button")
            .located(By.cssSelector("form[data-form='comment'] button[type='submit']"));
    public static final Target FIRST_PROJECT_COMMENTS = Target.the("project comments button")
            .located(By.cssSelector("button[data-action='project-comments']"));
    public static final Target LIKE = Target.the("like button")
            .located(By.cssSelector("button[data-action='react'][data-type='ME_GUSTA']"));
    public static final Target REMOVE_REACTION = Target.the("remove reaction button")
            .located(By.cssSelector("button[data-action='unreact']"));
    public static final Target LOGOUT = Target.the("sign out button")
            .located(By.cssSelector("button[data-action='logout']"));
    public static final Target TOAST = Target.the("notification message").located(By.id("toast"));

    private DevNetUi() {
    }
}
