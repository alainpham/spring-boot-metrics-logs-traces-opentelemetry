package mlt;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendMessagecontroller {
    private static final Logger logger = LoggerFactory.getLogger(SendMessagecontroller.class);

    @PostMapping("/send-msg")
    public Map<String, Object> sendMsg(@RequestBody Map<String, Object> message) {

        logger.info(message.toString());
        return message;
    }

    @GetMapping("/hello")
    public Map<String, Object> sendMsg() {
        logger.info("hello was requested");
        HashMap<String,Object> message =  new HashMap<String, Object>();
        message.put("msg", "hello");
        return message;
    }
}
