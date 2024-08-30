package cn.sanjin.video;

import cn.sanjin.video.common.entity.Lecture;
import cn.sanjin.video.common.entity.Video;
import cn.sanjin.video.common.entity.Videos;
import cn.sanjin.video.common.utils.PathUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class VideoApplication {

	public static Videos mathVideos = null;

	public static Videos physicsVideos = null;

	public static List<Lecture> mathLectures = new ArrayList<>();

	public static Path resourceDir = Paths.get("C:\\Users\\10326\\sanjin-file\\my_project\\1.video-player\\frontend");
	public static Path videosDir = resourceDir.resolve("暑假班");

	public static Path notesDir = resourceDir.resolve("暑假班").resolve("笔记");
	public static Path lecturesDir = resourceDir.resolve("暑假班").resolve("讲义");
	public static Path homeworkDir = resourceDir.resolve("暑假班").resolve("课后作业");


	public static void main(String[] args) {
		SpringApplication.run(VideoApplication.class, args);
	}

	@PostConstruct
	public void init() {
		initVideos();

		initLectures();
	}

	private static void initVideos() {
		mathVideos = new Videos();
		mathVideos.list = new ArrayList<>();
		if (videosDir.toFile().exists() && videosDir.toFile().isDirectory()) {
			Arrays.stream(videosDir.toFile().listFiles(File::isFile)).forEach(
					file -> {
						String name = file.getName().substring(0, file.getName().indexOf(".mp4")); // 删除后缀名
						String absolutePath = file.getAbsolutePath().toString();
						mathVideos.list.add(new Video(name,
								PathUtils.getResourcePath(resourceDir.toString(), absolutePath)));
					}
			);
		} else {
			assert false;
		}
		mathVideos.list.sort(Video::compareTo);
	}

	public void initLectures() {
		if (lecturesDir.toFile().exists() && lecturesDir.toFile().isDirectory()) {
			Arrays.stream(lecturesDir.toFile().listFiles(File::isFile)).forEach(
					file -> {
						String name = file.getName().substring(0, file.getName().indexOf(".pdf")); // 删除后缀名
						String absolutePath = file.getAbsolutePath().toString();
						mathLectures.add(new Lecture(name,
								PathUtils.getResourcePath(resourceDir.toString(), absolutePath)));
					}
			);
		} else {
			assert false;
		}
	}
}
