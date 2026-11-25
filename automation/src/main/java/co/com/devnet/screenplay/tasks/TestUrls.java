package co.com.devnet.screenplay.tasks;

final class TestUrls {
    private TestUrls() {
    }

    static String frontend() {
        String configured = System.getenv("DEVNET_FRONTEND_URL");
        String baseUrl = configured == null || configured.isBlank()
                ? "http://127.0.0.1:5173/"
                : configured;
        return baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
    }
}
