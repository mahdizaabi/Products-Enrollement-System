package com.mahdi.productmangementservice.intercomm;


import com.mahdi.productmangementservice.model.Xer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name= "user-service", url="localhost:8765")
public interface UserClientProxy {
    @RequestMapping(method = RequestMethod.POST, value="/service/names", consumes = "application/json")
    List<String> getUserNames(@RequestBody List<Long> userIdList);

    @PostMapping("/user-service/test-rest")
    ResponseEntity<Xer> test(@RequestBody Xer xer);

}