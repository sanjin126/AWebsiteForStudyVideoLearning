package cn.sanjin.video;

import cn.sanjin.video.common.entity.Lecture;
import cn.sanjin.video.common.entity.Note;
import cn.sanjin.video.common.entity.Video;
import cn.sanjin.video.common.entity.Videos;
import cn.sanjin.video.common.utils.PathUtils;
import cn.sanjin.video.user.config.ResourceConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.UrlResource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@SpringBootApplication
public class VideoApplication {

	static Logger log = Logger.getLogger(VideoApplication.class.getName());

	@Autowired
	private ResourceConfig resourceConfig;

	public static Videos mathVideos = null;

	public static Videos physicsVideos = null;

	public static List<Lecture> mathLectures;
	public static List<Note> mathNotes = null;

	public static Path resourceDir = Paths.get("C:\\Users\\10326\\sanjin-file\\my_project\\1.video-player\\frontend");
	public static Path videosDir = resourceDir.resolve("暑假班");

	public static Path notesDir = resourceDir.resolve("暑假班").resolve("笔记");
	public static Path lecturesDir = resourceDir.resolve("暑假班").resolve("讲义");
	public static Path homeworkDir = resourceDir.resolve("暑假班").resolve("课后作业");

	@Deprecated
	public void updateAfterResourceDirChange() {
		videosDir = resourceDir.resolve("暑假班");
		notesDir = resourceDir.resolve("暑假班").resolve("笔记");
		lecturesDir = resourceDir.resolve("暑假班").resolve("讲义");
		homeworkDir = resourceDir.resolve("暑假班").resolve("课后作业");
		initVideos();
		initLectures();

		try {
			resourceConfig.pathResourceResolver.setAllowedLocations(
					new UrlResource("file:"+ VideoApplication.resourceDir.toAbsolutePath()+File.separator)
			);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}

//		Field registrations = null;
//		try {
//			registrations = resourceConfig.registry.getClass().getDeclaredField("registrations");
//		} catch (NoSuchFieldException e) {
//			throw new RuntimeException(e);
//		}
//		registrations.setAccessible(true);
//		try {
//			((List<ResourceHandlerRegistration>) registrations.get(resourceConfig.registry))
//					.stream().forEach(
//							r->{
//								r.addResourceLocations("file:"+ VideoApplication.resourceDir.toAbsolutePath()+File.separator);
//							}
//					);
//		} catch (IllegalAccessException e) {
//			throw new RuntimeException(e);
//		}

	}




	public static void main(String[] args) {
		if (args.length == 1) {
			String resourcePath = args[0];
			resourceDir = Paths.get(resourcePath);
			log.info("before:-----sanjin----resourceDir:"+resourceDir);
		}
		SpringApplication.run(VideoApplication.class, args);
	}

	@PostConstruct
	public static void init() {
		log.info("-----sanjin----init videos and lectures");
		log.info("-----sanjin----resourceDir:"+resourceDir);
		log.info("-----sanjin----videosDir:"+videosDir);
		log.info("-----sanjin----notesDir:"+notesDir);
		log.info("-----sanjin----lecturesDir:"+lecturesDir);
		log.info("-----sanjin----homeworkDir:"+homeworkDir);
		videosDir = resourceDir.resolve("暑假班");
		notesDir = resourceDir.resolve("暑假班").resolve("笔记");
		lecturesDir = resourceDir.resolve("暑假班").resolve("讲义");
		homeworkDir = resourceDir.resolve("暑假班").resolve("课后作业");
		initVideos();

		initLectures();

		initNotes();
	}

	public static void initVideos() {
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
			log.info("sanjin------videosDir not exists");
			return;
		}
		log.info("sanjin------length:"+videosDir.toFile().listFiles(File::isFile).length);
		mathVideos.list.sort(Video::compareTo);
	}

	public static void initLectures() {
		mathLectures = new ArrayList<>();
		if (lecturesDir.toFile().exists() && lecturesDir.toFile().isDirectory()) {
			Arrays.stream(lecturesDir.toFile().listFiles(File::isFile)).forEach(
					file -> {
						String name = file.getName().substring(0, file.getName().indexOf(".pdf")); // 删除后缀名
						String absolutePath = file.getAbsolutePath();
						mathLectures.add(new Lecture(name,
								PathUtils.getResourcePath(resourceDir.toString(), absolutePath)));
					}
			);
		} else {
			log.info("sanjin------lecturesDir not exists");
			return;
		}
	}

	public static void initNotes() {
		mathNotes = new ArrayList<>();
		if (notesDir.toFile().exists() && notesDir.toFile().isDirectory()) {
			Arrays.stream(notesDir.toFile().listFiles(File::isFile)).forEach(
					file -> {
						String name = file.getName().substring(0, file.getName().indexOf(".pdf")); // 删除后缀名
						String absolutePath = file.getAbsolutePath();
						mathNotes.add(new Note(name,
								PathUtils.getResourcePath(resourceDir.toString(), absolutePath)));
					}
			);
		} else {
			log.info("sanjin------notesDir not exists");
			return;
		}
	}
}
