package cn.sanjin.video.common.entity;

public class Lecture {
    private String title;
    private String url;

    public Lecture(String name, String resourcePath) {
        this.title = name;
        this.url = resourcePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
