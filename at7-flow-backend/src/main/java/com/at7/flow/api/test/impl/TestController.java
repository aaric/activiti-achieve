package com.at7.flow.api.test.impl;

import com.at7.flow.api.test.TestApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Test Controller
 *
 * @author Aaric
 * @version 0.1.0-SNAPSHOT
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/test/test")
public class TestController implements TestApi {

    @Override
    @GetMapping("/say")
    public String say(@RequestParam String name) {
        return "hello " + name;
    }

    @Override
    @PostMapping("/say")
    public String say(@RequestBody TestUser user) {
        return say(user.getName());
    }
}
