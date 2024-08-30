package cn.sanjin.video.user.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.AbstractResourceResolver;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("addResourceHandlers");
        /**
         * https://stackoverflow.com/questions/21123437/how-do-i-use-spring-boot-to-serve-static-content-located-in-dropbox-folder
         */
        registry.addResourceHandler("/resource/**")
                .addResourceLocations("file:C:\\Users\\10326\\sanjin-file\\my_project\\1.video-player\\frontend\\")
                .resourceChain(true)
//                .addResolver(new AbstractResourceResolver() {
//                    @Override
//                    protected Resource resolveResourceInternal(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
//                        try {
//                            for (Resource location : locations) {
//                                String absolutePath = location.createRelative((requestPath)).getURL().getPath();
//                                absolutePath = URLDecoder.decode(absolutePath, "UTF-8");
//                                File file = new File(absolutePath);
//                                if (file.isDirectory()) {
//                                    String finalAbsolutePath = absolutePath;
//                                    return new AbstractResource() {
//                                        final String directoryPath = finalAbsolutePath;
//                                        final File directory = file;
//                                        @Override
//                                        public String getDescription() {
//                                            return "dir:" + directoryPath;
//                                        }
//
//                                        @Override
//                                        public InputStream getInputStream() throws IOException {
//                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                                            Arrays.stream(directory.listFiles()).forEach(
//                                                    f -> {
//                                                        try {
//                                                            baos.write((f.getName() + "\n").getBytes());
//                                                        } catch (IOException e) {
//                                                            throw new RuntimeException(e);
//                                                        }
//                                                    }
//                                            );
//                                            return new ByteArrayInputStream(baos.toByteArray());
//                                        }
//
//                                        @Override
//                                        public long lastModified() throws IOException {
//                                            return directory.lastModified();
//                                        }
//                                    };
//                                } else {
//                                    return null;
//                                }
//                            }
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                        return null;
//                    }
//
//                    @Override
//                    protected String resolveUrlPathInternal(String resourceUrlPath, List<? extends Resource> locations, ResourceResolverChain chain) {
//                        return resourceUrlPath;
//                    }
//                })
                .addResolver(new PathResourceResolver())
        ;
    }

}
