package co.com.devnet.screenplay.tasks;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public final class TestUrls {
    private TestUrls() {
    }

    public static String frontend() {
        String envUrl = System.getenv("DEVNET_FRONTEND_URL");
        String sysPropUrl = System.getProperty("DEVNET_FRONTEND_URL",
                System.getProperty("pages.devnetUrl", System.getProperty("webdriver.base.url")));
        String configured = (envUrl != null && !envUrl.isBlank()) ? envUrl : sysPropUrl;
        if (configured == null || configured.isBlank()) {
            configured = readFromEnvFile("DEVNET_FRONTEND_URL");
        }
        String baseUrl = (configured == null || configured.isBlank())
                ? "http://127.0.0.1:5173/"
                : configured.trim();
        return baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
    }

    private static String readFromEnvFile(String key) {
        List<Path> candidates = List.of(
                Paths.get(".env"),
                Paths.get("automation", ".env"),
                Paths.get("..", ".env")
        );
        for (Path p : candidates) {
            if (Files.isRegularFile(p)) {
                try {
                    for (String line : Files.readAllLines(p)) {
                        String t = line.trim();
                        if (t.startsWith(key + "=")) {
                            return t.substring(key.length() + 1).trim().replaceAll("^[\"']|[\"']$", "");
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        }
        return null;
    }
}
