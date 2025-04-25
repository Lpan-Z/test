package FaceApp.java;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class UserController {
    @RequestMapping("/sayHi")
    public String sayHi(){
        return "hello,Spring";
    }
}
