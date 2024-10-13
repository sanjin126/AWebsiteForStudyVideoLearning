package cn.sanjin.video.common.utils;

import java.io.File;

public class PathUtils {

    /**
     * https://stackoverflow.com/questions/34129054/patternsyntaxexception-unexpected-internal-error-near-index-1-for-splitfile
     */
    public static final String SPLIT = File.separator.replace("\\","\\\\");
    // 得到一个path相对于另一个path的相对路径
    public static String getRelativePath(String basePath, String path) {
        String[] base = basePath.split(SPLIT);
        String[] p = path.split(SPLIT);
        int i = 0;
        for (; i < base.length && i < p.length; i++) {
            if (!base[i].equals(p[i])) {
                break;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int j = i; j < base.length; j++) {
            sb.append(".." + SPLIT);
        }
        for (int j = i; j < p.length; j++) {
            sb.append(p[j]);
            if (j != p.length - 1) {
                sb.append(File.separator); // use separator instead of SPLIT, SPLIT is not a system separator, it's only a regex separator used for split
            }
        }
        return sb.toString();
    }

    public static String getResourcePath(String resourceBasePath, String resourcePath) {
        // " File.separator + "indicates that the path is relative to the root url
        return File.separator + "resource"+ File.separator +getRelativePath(resourceBasePath, resourcePath);
    }

    public static void main(String[] args) {
        System.out.println(getRelativePath("C:\\Users\\10326\\sanjin-file\\my_project\\1.video-player\\frontend", "C:\\Users\\10326\\sanjin-file\\my_project\\1.video-player\\frontend\\暑假班\\1.mp4"));
    }
}
