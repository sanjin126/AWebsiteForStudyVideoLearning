package cn.sanjin.video.admin.controller;

import cn.sanjin.video.common.entity.Video;
import cn.sanjin.video.common.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @PostMapping("/video")
    public Result<Video> addVideoInfo(@RequestBody Video v) {
        System.out.println(v);
        return new Result.Builder<Video>().message("success")
                .status(200).build();
    }


}
