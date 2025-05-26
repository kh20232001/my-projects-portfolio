package com.api._dummy;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.jobpal.common.base.BaseResponse;
import com.api.jobpal.common.base.ResponseMessage;

@RestController
@RequestMapping("dummy")
public class DummyController {

    @GetMapping("/hello")
    public BaseResponse getHello() {

        DummyData data = new DummyData();
        data.setComment("Hello, World!");
        data.setResult(true);

        return BaseResponse.success(data, ResponseMessage.SUCCESS);
    }

    @GetMapping("/result")
    public BaseResponse getResult(@RequestParam("expected") boolean expected) {

        DummyData data = new DummyData();
        if (expected) {
            data.setComment("Success!");
            data.setResult(true);
        } else {
            data.setComment("Failed!");
            data.setResult(false);
        }

        return BaseResponse.success(data, ResponseMessage.SUCCESS);
    }

}
