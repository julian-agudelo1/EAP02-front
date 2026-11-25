package co.com.devnet.screenplay.stepdefinitions;

import java.util.UUID;

final class TestData {
    private final String runId = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    final String username = "e2e" + runId;
    final String email = "devnet.e2e." + runId + "@example.com";
    final String password = "DevNet!A7x" + runId;
    final String projectTitle = "E2E project " + runId;
    final String projectDescription = "Automated acceptance project for run " + runId + ".";
    final String discussionTitle = "E2E question " + runId;
    final String discussionBody = "Question created by automated acceptance tests for run " + runId + ".";
    final String comment = "E2E comment " + runId;
    String originalLoginError;
    String traceId;
    String authorProfileHash;
    int originalReactionCount;

    static String frontendUrl() {
        String configured = System.getenv("DEVNET_FRONTEND_URL");
        String baseUrl = configured == null || configured.isBlank()
                ? "http://127.0.0.1:5173/"
                : configured;
        return baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
    }

    static String adminEmail() {
        return requiredEnvironmentVariable("DEVNET_ADMIN_EMAIL");
    }

    static String adminPassword() {
        return requiredEnvironmentVariable("DEVNET_ADMIN_PASSWORD");
    }

    private static String requiredEnvironmentVariable(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Set " + name + " locally before running privileged scenarios.");
        }
        return value;
    }
}
