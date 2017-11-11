package com.example.zuul.auth.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


//@FeignClient(name="tokenclient",url="http://localhost:9090/")
public interface TokenAuthClient {
	
	@RequestMapping(value="/getTokenForUser", method=RequestMethod.GET)
    public String userToken(@RequestHeader String roles, @RequestHeader String userName);

}
