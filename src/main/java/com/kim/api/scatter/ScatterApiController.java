package com.kim.api.scatter;

import com.kim.api.core.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 머니뿌리기 API
 */
@Slf4j
@RequestMapping("/api/scatter")
@RestController
public class ScatterApiController {

    /**
     * 뿌리기
     *
     * @return 응답
     */
    @PostMapping
    public CommonResponse scatter(@Valid @RequestBody String request) {
        return null;
    }
}

