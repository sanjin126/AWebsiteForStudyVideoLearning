package cn.sanjin.video.user.controller;

import cn.sanjin.video.ResourceContext;
import cn.sanjin.video.VideoApplication;
import cn.sanjin.video.common.entity.Video;
import cn.sanjin.video.common.utils.PathUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Node;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("")
public class UserController {

    @GetMapping("user/videolist/{navigator}")
    @CrossOrigin()
    @ResponseBody
    public List<Video> getVideoListBy(@PathVariable("navigator") String navigator) throws IOException {
        String base_dirname = ResourceContext.navigator_2_base_dirname.get(navigator);
        if (base_dirname == null) {
            throw new RuntimeException("navigator not found");
        }
        Path videoAbsPath = Path.of(ResourceContext.RESOURCE_ABSOLUTE_PATH, base_dirname, ResourceContext.VIDEO_DIR_NAME);

        class RemoveSuffix {
            public static String removeSuffix(String filename) {
                return filename.substring(0, filename.lastIndexOf('.'));
            }
        }

        // 将video目录下的文件转为video list 返回
        return Files
                .list(videoAbsPath)
                .map((video) -> new Video(RemoveSuffix.removeSuffix(video.getFileName().toString()),
                        PathUtils.getResourcePath(ResourceContext.RESOURCE_ABSOLUTE_PATH,
                                video.toFile().getAbsolutePath())))
                .collect(Collectors.toList());
    }

    @GetMapping()
    public String indexPage(Model model) {
        model.addAttribute("lecturePath", "directory/math/lecture");
        model.addAttribute("notePath", "directory/math/note");
        model.addAttribute("jsPath",
                PathUtils.getResourcePath(VideoApplication.resourceDir.toString(),
                        VideoApplication.resourceDir.resolve("index.js").toString()));
        return "index";
    }

    @GetMapping("directory/{subject}/{type}")
    public String directoryPage(Model model, @PathVariable String subject, @PathVariable String type) {
        switch (subject) {
            case "math":
                switch (type) {
                    case "lecture":
                        model.addAttribute("fileList", VideoApplication.mathLectures);
                        break;
                    case "note":
                        model.addAttribute("fileList", VideoApplication.mathNotes);
                        break;
                    default:
                        assert false;
                }
                break;
            case "physics":

                break;
            default:
                assert false;
        }
        model.addAttribute("jsPath",
                PathUtils.getResourcePath(VideoApplication.resourceDir.toString(),
                        VideoApplication.resourceDir.resolve("directory.js").toString()));
        return "directory";
    }

    @GetMapping("user/video/math")
    @CrossOrigin()
    @ResponseBody
    public List<Video> getMathVideoList() {
        return VideoApplication.mathVideos.list;
    }

    @GetMapping("user/video/physics")
    @CrossOrigin
    @ResponseBody
    public List<Video> getPhysiscsVideoList() {
        return VideoApplication.physicsVideos.list;
    }

    @GetMapping("user/lecture/math")
    @CrossOrigin
    @ResponseBody
    public List<Video> getMathLectureList() {
        return VideoApplication.mathVideos.list;
    }

    public enum CommentType {
        video,
        discuss
    }

    @Autowired
    RedisTemplate<String, String> template;

    @PostMapping("user/comment")
    @ResponseBody
    @CrossOrigin
    public void commentOn(CommentType type, Integer storeId, Long timestamp, Integer parentId, String content) {
        switch (type) {
            case discuss -> {
                template.opsForZSet().add("DISCUSS", content, timestamp);
            }
            case video -> {
                ObjectMapper mapper = new ObjectMapper();
                Integer id = allocateCommentId();
                HashMap<String, String> ctx = new HashMap<>();
                ctx.put("id", id.toString());
                ctx.put("content", content);
                ctx.put("parentId", parentId.toString());
                try {
                    template.opsForZSet().add("VIDEO::" + storeId.toString(), mapper.writeValueAsString(ctx), timestamp);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
            default -> {
            }
        }
    }

    public static class Comment implements Serializable {
        Long timestamp;
        String content;
        Integer parentId;
        Integer id;
        Integer storeId;


        public Comment(Long timestamp, String content, Integer parentId, Integer id, Integer storeId) {
            this.timestamp = timestamp;
            this.content = content;
            this.parentId = parentId;
            this.id = id;
            this.storeId = storeId;
        }


        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Integer getParentId() {
            return parentId;
        }

        public void setParentId(Integer parentId) {
            this.parentId = parentId;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getStoreId() {
            return storeId;
        }

        public void setStoreId(Integer storeId) {
            this.storeId = storeId;
        }
    }

    @GetMapping("user/comment/allocateId")
    @ResponseBody
    public Integer allocateCommentId() {
        return Math.toIntExact(template.opsForValue().increment("COMMENT_ID"));
    }


    @GetMapping("/user/comment")
    @CrossOrigin
    @ResponseBody
    public String handleGetComment(CommentType type, Integer storeId) {
        Set<ZSetOperations.TypedTuple<String>> set = null;
        switch (type) {
            case discuss -> {
                set = template.opsForZSet().rangeWithScores("DISCUSS", 0, Long.MAX_VALUE);
            }
            case video -> {
                set = template.opsForZSet().rangeWithScores("VIDEO::" + storeId, 0, Long.MAX_VALUE);
            }
            default -> {
            }
        }
        List<Comment> comments = set.stream()
                .map(
                        e -> {
                            try {
                                long timeStamp = e.getScore().longValue();
                                Map<String, String> map = new ObjectMapper().readValue(e.getValue(), HashMap.class);
                                map.put("timestamp", Long.toString(timeStamp));
                                map.put("storeId", storeId.toString());
                                map.forEach((k, v) -> System.out.println(k + ":" + v));
                                return map;
                            } catch (JsonProcessingException jsonProcessingException) {
                                throw new RuntimeException(jsonProcessingException);
                            }
                        }
                )
                .map(
                        e -> new Comment(Long.parseLong(e.get("timestamp")), e.get("content"), Integer.parseInt(e.get("parentId")), Integer.parseInt(e.get("id")), Integer.parseInt(e.get("storeId")))
                )
                .collect(Collectors.toList());
        // 将comments转为森林结构，以json格式返回
        class Node {
            Comment comment;
            List<Node> children;

            public Node(Comment comment, List<Node> children) {
                this.comment = comment;
                this.children = children;
            }

            public Comment getComment() {
                return comment;
            }

            public void setComment(Comment comment) {
                this.comment = comment;
            }

            public List<Node> getChildren() {
                return children;
            }

            public void setChildren(List<Node> children) {
                this.children = children;
            }
        }
        comments.add(new Comment(0L, "root", null, 0, storeId));
        Map<Integer, Node> map = new HashMap<>();
        comments.forEach(
                e -> {
                    map.put(e.id, new Node(e, null));
                }
        );
        comments.forEach(
                e -> {
                    if (e.parentId != null) {
                        Node parent = map.get(e.parentId);
                        if (parent.children == null) {
                            // 使用modified list
                            parent.children = new ArrayList<>();
                            parent.children.add(map.get(e.id));
                        } else {
                            parent.children.add(map.get(e.id));
                        }
                    }
                }
        );
        try {
            return new ObjectMapper().writeValueAsString(map.get(0));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
