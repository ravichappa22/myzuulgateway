package com.example.zuul.filters;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class MyZuulPreFilter extends ZuulFilter {
	
	/*@Autowired
	private TokenAuthClient tokenAuthClient;*/
	
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
        ctx.getResponse().setStatus(200); 
       
        // first set the static response. while next filters execute this response will be overriden, if system down this default response will be present 
        if (ctx.getResponseBody() == null ) { 
            ctx.setResponseBody("static content"); 
            //ctx.setSendZuulResponse(true); 
        } 
             

		String smUser = ctx.getRequest().getHeader("SM_USER");
		String smUserGroups = ctx.getRequest().getHeader("SM_USERGROUPS");
		if(smUser == null || smUserGroups == null){
			ctx.setSendZuulResponse(false);
		}else{
			String token = null;
			//tokenAuthClient.userToken(smUserGroups, smUser);
			//restTemplate.getForObject("http://localhost:9090/getTokenForUser", String.class, new String[]{smUserGroups,smUser});
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("roles", smUserGroups);
			httpHeaders.add("userName", smUser);
			HttpEntity <String> httpEntity = new HttpEntity <String> (null, httpHeaders);
			ResponseEntity<String> response = null;
	        try{
			    response = restTemplate.exchange("http://localhost:8080/auth/getTokenForUser", HttpMethod.GET, httpEntity, String.class);
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
	        
			System.out.println("Token we got>>>= " + response);
	        Map<String, String> map = new HashMap<String, String>();
			map.put("Authorization", "bearer "+response.getBody());
			map.put("SM_USER", smUser);
			map.put("SM_USERGROUPS", smUserGroups);
			ctx.getZuulRequestHeaders().putAll(map);
	       
		}
		return null;
	}

	@Override
	public boolean shouldFilter() {
		
		return true;
	}

	@Override
	public int filterOrder() {
		
		return 0;
	}

	@Override
	public String filterType() {
		
		return "pre";
	}

}
