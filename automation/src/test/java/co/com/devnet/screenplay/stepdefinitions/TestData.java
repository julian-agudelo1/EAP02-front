package co.com.devnet.screenplay.stepdefinitions;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

final class TestData {
    private static final Map<String, String> FILE_CONFIG = loadLocalConfig();
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
        return co.com.devnet.screenplay.tasks.TestUrls.frontend();
    }

    static String adminEmail() {
        return getConfiguration("DEVNET_ADMIN_EMAIL", "admin@devnet.com");
    }

    static String adminPassword() {
        return getConfiguration("DEVNET_ADMIN_PASSWORD", "Admin123*");
    }

    static String getConfiguration(String name, String defaultValue) {
        String sysProp = System.getProperty(name);
        if (sysProp != null && !sysProp.isBlank()) {
            return sysProp.trim();
        }

        String env = System.getenv(name);
        if (env != null && !env.isBlank()) {
            return env.trim();
        }

        String fileVal = FILE_CONFIG.get(name);
        if (fileVal != null && !fileVal.isBlank()) {
            return fileVal.trim();
        }

        return defaultValue;
    }

    private static Map<String, String> loadLocalConfig() {
        Map<String, String> map = new HashMap<>();
        List<Path> candidates = List.of(
                Paths.get(".env"),
                Paths.get("automation", ".env"),
                Paths.get("..", ".env"),
                Paths.get("serenity.properties"),
                Paths.get("automation", "serenity.properties"),
                Paths.get("..", "serenity.properties")
        );
        for (Path path : candidates) {
            if (Files.isRegularFile(path)) {
                try {
                    for (String line : Files.readAllLines(path)) {
                        String trimmed = line.trim();
                        if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                            continue;
                        }
                        int idx = trimmed.indexOf('=');
                        if (idx > 0) {
                            String key = trimmed.substring(0, idx).trim();
                            String val = trimmed.substring(idx + 1).trim()
                                    .replaceAll("^[\"']|[\"']$", "");
                            map.putIfAbsent(key, val);
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        }
        return map;
    }
}
