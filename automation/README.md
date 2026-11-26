# DevNet E2E automation

This is a separate Java 17 / Serenity Screenplay project generated with the
[ScreenplayArchitecture Gradle plugin](https://github.com/jumosqu12/ScreenplayArchitecture).
It does not replace or modify the Vite frontend configuration.

## Run locally

Start the frontend from the repository root in one PowerShell terminal:

```powershell
npm.cmd run dev
```

By default, Vite proxies `/api` to `https://eap02-back.onrender.com`. Only run
write scenarios against an isolated test backend. Point the Vite proxy to that
backend by setting `DEVNET_API_TARGET` in the root `.env` file.

In another terminal, run the Screenplay tests and generate the Serenity report:

```powershell
gradle -p automation clean test aggregate
```

You can also run individual runners per feature:

```powershell
# Registro de cuenta (HU-01)
gradle -p automation test --tests "co.com.devnet.screenplay.runners.AccountRegistrationRunner" aggregate

# Inicio de sesión (HU-02)
gradle -p automation test --tests "co.com.devnet.screenplay.runners.UserLoginRunner" aggregate

# Publicación de proyectos (HU-07)
gradle -p automation test --tests "co.com.devnet.screenplay.runners.ProjectPublicationRunner" aggregate

# Comentarios (HU-13)
gradle -p automation test --tests "co.com.devnet.screenplay.runners.CommentsRunner" aggregate

# Reacciones (HU-14)
gradle -p automation test --tests "co.com.devnet.screenplay.runners.ReactionsRunner" aggregate

# Mantenimiento de proyectos (HU-08 / Pending UI)
gradle -p automation test --tests "co.com.devnet.screenplay.runners.ProjectMaintenanceRunner" aggregate
```

The HTML report is generated at `automation\target\site\serenity\index.html`.
The default runner excludes scenarios tagged `@pending-ui` and
`@requires-admin`. HU-08 is tagged `@pending-ui` because this frontend currently
has no project edit/delete controls. Privileged scenarios require an isolated
test administrator and are kept out of the default run until one is configured.

Set `DEVNET_FRONTEND_URL` to override the local frontend URL. Browser automation
uses Chrome in headless mode and records screenshots for failures.

## Test data and scope

The suite creates unique test accounts and content on the configured backend.
The current UI has no project deletion flow, so test projects remain in the
isolated test environment. Comment and reaction scenarios remove their own
temporary records where the UI supports it.

Invalid email, password, project, and comment scenarios submit through the
application event handler after disabling browser-only validation constraints;
the assertions therefore check server responses rather than HTML validation.

Some criteria are not currently verifiable from the frontend alone. Failed
login auditing and administrator comment deletion need a test administrator
with the relevant permissions. Project edit/delete tests remain pending until
the frontend provides those controls.
