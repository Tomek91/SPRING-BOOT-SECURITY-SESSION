package pl.com.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.app.dto.rest.ResponseMessage;

@RestController
public class MainController {

    @GetMapping("/testUser")
    public ResponseMessage<String> testUser() {
        return ResponseMessage.<String>builder().data("USER").build();
    }

    @GetMapping("/testAdmin")
    public ResponseMessage<String> testAdmin() {
        return ResponseMessage.<String>builder().data("ADMIN").build();
    }

}
