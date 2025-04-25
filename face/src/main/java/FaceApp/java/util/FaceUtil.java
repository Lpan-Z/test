package FaceApp.java.util;


import com.aliyun.facebody20191230.Client;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.stereotype.Component;


/**
 * @Author 12629
 * @Description：
 */
@Component
public class FaceUtil {

    public  Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        /*
          初始化配置对象com.aliyun.teaopenapi.models.Config
          Config对象存放 AccessKeyId、AccessKeySecret、endpoint等配置
         */
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "facebody.cn-shanghai.aliyuncs.com";
        return new com.aliyun.facebody20191230.Client(config);
    }

}
