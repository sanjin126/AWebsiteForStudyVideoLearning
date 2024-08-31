package cn.sanjin.video.user.controller;

import cn.sanjin.video.VideoApplication;
import cn.sanjin.video.common.entity.Video;
import cn.sanjin.video.common.utils.PathUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("")
public class UserController {

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
}
