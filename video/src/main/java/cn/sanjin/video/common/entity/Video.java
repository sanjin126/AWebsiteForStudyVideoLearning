package cn.sanjin.video.common.entity;

import java.util.Objects;

public class Video implements Comparable<Video>{
    String title;
    String url;

    public Video() {
    }

    public Video(String title, String url) {
        this.title = title;
        this.url = url;
    }

    @Override
    public String toString() {
        return "Video{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Video video)) return false;
        return Objects.equals(title, video.title) && Objects.equals(url, video.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, url);
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

    public static void main(String[] args) {
        Video video = new Video("9-2", "");
        Video video1 = new Video("09", "");
        video1.compareTo(video);
    }

    @Override
    public int compareTo(Video o) {
        double valOfThis = 0;
        double valOfOther = 0;
        if (this.title.substring(0,2).matches("\\d\\d")) {
            if (this.title.substring(0,1).equals("0")) {
                valOfThis = Integer.parseInt(this.title.substring(1,2));
            } else  {
                valOfThis = Integer.parseInt(this.title.substring(0,2));
            }
        } else {
            valOfThis = Integer.parseInt(this.title.substring(0,1)) + 0.5;
        }
        if (o.title.substring(0,2).matches("\\d\\d")) {
            if (o.title.substring(0,1).equals("0")) {
                valOfOther = Integer.parseInt(o.title.substring(1,2));
            } else  {
                valOfOther = Integer.parseInt(o.title.substring(0,2));
            }
        } else {
            valOfOther = Integer.parseInt(o.title.substring(0,1)) + 0.5;
        }
        return (int) ((valOfThis - valOfOther) * 2); //*2是为了在0.5时候向上取
    }
}
