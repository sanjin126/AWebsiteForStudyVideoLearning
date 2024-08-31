package cn.sanjin.video.admin.controller;

import cn.sanjin.video.VideoApplication;
import cn.sanjin.video.common.entity.Video;
import cn.sanjin.video.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    VideoApplication videoApplication;

    @PostMapping("/video")
    public Result<Video> addVideoInfo(@RequestBody Video v) {
        System.out.println(v);
        return new Result.Builder<Video>().message("success")
                .status(200).build();
    }

    /**
     * 错误的实现，
     * 因为虽然修改了resourceDir，但是没有ResourceConfig中的pathResourceResolver的allowedLocations，
     * 或者更确切的定位 ResourceHttpRequestHandler 的 resourceLocations
     * @param resourceDir
     * @return
     */
    @PostMapping("/resourceDir")
    public Result<String> setResourceDir(@RequestParam String resourceDir) {
        Path resourcePath = Path.of(resourceDir);
        if (resourcePath.toFile().exists()) {
            VideoApplication.resourceDir = resourcePath;
            videoApplication.updateAfterResourceDirChange();
            return new Result.Builder<String>().message("success")
                    .status(200)
                    .data(VideoApplication.resourceDir.toAbsolutePath().toString())
                    .build();
        } else {
            return new Result.Builder<String>().message("fail")
                    .status(400)
                    .data("no valid dir path: "+resourcePath.toAbsolutePath().toString())
                    .build();
        }

    }


}
