package server.springapi;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class RsbotRestController {

    private Log logger = LogFactory.getLog(RsbotRestController.class);

    @CrossOrigin
    @PostMapping("/login")
    public String getLogin() {
        return "success";
    }

    @GetMapping("/api")
    public String handleGet() {
        return "asdf";
    }

}
