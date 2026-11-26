package co.com.devnet.screenplay.models;

public class ProjectModel {
    private final String title;
    private final String description;
    private final String technology;

    public ProjectModel(String title, String description, String technology) {
        this.title = title;
        this.description = description;
        this.technology = technology;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTechnology() {
        return technology;
    }
}
