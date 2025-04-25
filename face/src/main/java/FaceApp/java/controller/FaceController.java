package FaceApp.java.controller;


import FaceApp.java.service.FaceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


//把将来返回的数据打包好  注解
@RestController
//前端请求这个路径
@RequestMapping("/face")
//解决跨域问题
@CrossOrigin(origins = "*")
public class FaceController {


    //可以帮我获取一个 FaceServiceImpl 对象
    @Autowired
    FaceServiceImpl faceService;

    /**
     * @param file  前端传给后端的照片
     * @param style 前端传给后端的风格参数
     * @return
     */
    @PostMapping("/generateHumanAnimeStyle")
    public String generateHumanAnimeStyle(@RequestBody MultipartFile file,
                                          @RequestParam String style) throws Exception {
        //调用FaceServiceImpl中的generateHumanAnimeStyle方法完成业务处理
        return faceService.generateHumanAnimeStyle(file, style);
    }


    @PostMapping("/isLivingFace")
    public boolean isLivingFace(@RequestBody MultipartFile file) throws Exception {
        return faceService.isLivingFace(file);
    }
}