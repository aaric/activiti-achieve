package com.at7.flow.api.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.util.Date;

/**
 * Test Api
 *
 * @author Aaric
 * @version 0.1.0-SNAPSHOT
 */
@Tag(name = "Test API")
public interface TestApi {

    @Operation(summary = "Say")
    String say(@Parameter(description = "Name", example = "world") String name);

    @Operation(summary = "Say")
    String say(@Parameter(description = "Test User") TestUser user);

    @Tag(name = "Test User")
    @Data
    class TestUser {

        @Schema(description = "Name")
        private String name;

        @Schema(description = "Aget")
        private int age;

        @Schema(description = "Birthday")
        private Date birthday;
    }
}
