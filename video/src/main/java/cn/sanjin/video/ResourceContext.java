package cn.sanjin.video;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResourceContext {
    //读取环境变量
    public static final String RESOURCE_ABSOLUTE_PATH = System.getenv("VIDEO_PLAYER_RESOURCE_PATH");

    public static void main(String[] args) {
        System.out.println(RESOURCE_ABSOLUTE_PATH);
    }
    // 用于前端的navigator转换为后端的base_dirname
    public static Map<String, String> navigator_2_base_dirname = Map.of(
            "赵礼显数学高二暑期视频", "zhaolixian-shuqi-video",
            "赵礼显数学高二秋季学期视频", "zhaolixian-qiuji-video"
    );

    public static List<String> resource_base_dirname_list = navigator_2_base_dirname.values().stream().collect(Collectors.toList());

    public static final String VIDEO_DIR_NAME = "video";

}
