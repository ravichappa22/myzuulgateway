package com.example.zuul.auth.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name="tokenclient",url="http://localhost:8080")
public interface TokenAuthClient {
	
	@RequestMapping(value="/auth/getTokenForUser", method=RequestMethod.GET)
    public OAuth2AccessToken userToken(@RequestHeader("groups") String groups, @RequestHeader("userName") String userName);
	
	@RequestMapping(value="/auth/oauth/check_token", method=RequestMethod.GET)
    public ResponseEntity<String> checkUserToken(@RequestParam("token") String token);
	
}
