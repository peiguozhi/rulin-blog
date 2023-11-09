package com.xuecheng.ucenter.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Mr.M
 * @version 1.0
 * @description TODO
 * @date 2023/2/24 14:45
 */
@FeignClient(value = "checkcode",fallbackFactory = CheckCodeClientFactory.class)
@RequestMapping("/checkcode")
public interface CheckCodeClient {

 @PostMapping(value = "/verify")
 public Boolean verify(@RequestParam("key") String key, @RequestParam("code") String code);

}
