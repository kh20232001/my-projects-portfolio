package com.api._dummy;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.jobpal.common.base.BaseResponse;
import com.api.jobpal.common.base.ResponseMessage;

@RestController
@RequestMapping("shop")
public class ShopDummyController {

  @PostMapping("/getitemlist")
  public BaseResponse getResult(@RequestParam("genre") int genre, @RequestParam("large") int large,
      @RequestParam("small") int small) {

    System.out.println("genre:" + genre);
    System.out.println("large:" + large);
    System.out.println("small:" + small);

    ShopDummyData data = new ShopDummyData(10001, 120, 0.0, 10, 0);

    return BaseResponse.success(data, ResponseMessage.SUCCESS);
  }

}
