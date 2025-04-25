package FaceApp.java.util;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
@Component
public class FileUtil {

    //该成员变量 可以存储到Constant类中
    private static final String PATH = "D:\\code\\JAVA\\path\\save";

    public File storageFile(MultipartFile multipartFile) throws IOException {

        File uploadDir = new File(PATH);

        //如果文件目录不存在，创建该目录
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 1.1.返回上传文件的原始文件名
        String originalFilename = multipartFile.getOriginalFilename();

        //1.2. 获取后缀名   古风.png
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));


        //1.3.使用随机的通用唯一识别码和扩展名 组合成新的文件名   12abdr453dfdsfa.png
        String newFileName = UUID.randomUUID().toString() + extension;



        //使用File.separator 文件路径的分隔符，确保跨平台兼容
        File destFile = new File(PATH + File.separator + newFileName);
        //1.4.保存上传文件到指定路径
        multipartFile.transferTo(destFile);

        return destFile;
    }
}