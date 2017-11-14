package com.example.zuul.filters;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class MyZuulPreFilter extends ZuulFilter {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${smpath.not.applicable}")
	private String smPathsNotApplicable;
	
	private List<String> pathsToSkip;
	
	@PostConstruct
	public void filterPostConsruct(){
		pathsToSkip = Arrays.asList(smPathsNotApplicable.split(","));
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
        ctx.getResponse().setStatus(200); 
       
        // first set the static response. while next filters execute this response will be overriden, if system down this default response will be present 
        if (ctx.getResponseBody() == null ) { 
            ctx.setResponseBody("static content"); 
        } 
        
		String smUser = ctx.getRequest().getHeader("SM_USER");
		String smUserGroups = ctx.getRequest().getHeader("SM_USERGROUPS");
		String oAuthToken = ctx.getRequest().getHeader("Authorization");
		ResponseEntity<String> tokenRespons = null;
		pathsToSkip.contains(ctx.getRequest().getRequestURI());
		if(pathsToSkip.contains(ctx.getRequest().getRequestURI())){
			//Enumeration<String> parameters= ctx.getRequest().getParameterNames();
			/*while(parameters.hasMoreElements()){
				String parameter = parameters.nextElement();
				System.out.println("parameter=" + parameter + "paramter Value=" + ctx.getRequest().getParameter(parameter));
			*/
				/*Map<String, List<String>> qps = new HashMap<String, List<String>>();
				//copy request param map
				Map<String, String[]> qpmap = ctx.getRequest().getParameterMap();
				StringBuilder buffer = new StringBuilder("?");
				for (Map.Entry<String, String[]> entry : qpmap.entrySet()) {
				String key = entry.getKey();
				System.out.println("key=" + key);
				String[] values = entry.getValue();
				System.out.println("values= " + values);
				qps.put(key, Arrays.asList(values));
				buffer.append(key+"="+values[0]+"&");
				
				}
				String  uRI= ctx.getRequest().getRequestURI().concat(buffer.toString().substring(0, buffer.toString().length()-1));
				System.out.println("Requested URL" + ctx.getRequest().getRequestURL());
				System.out.println("formed URI" + uRI);
				ctx.setRequestQueryParams(qps);*/
				
				
			//}
		 
			ctx.getZuulRequestHeaders().put("Authorization", oAuthToken);
			return null;
		}
		if (smUser == null || smUserGroups == null) {
			System.out.println("PRE FILTER >>SM Headers Not Present, So Not Forwarding the request");
			ctx.setSendZuulResponse(false);
		} else if (oAuthToken == null) {
			oAuthToken = getTheToken(ctx,smUser,smUserGroups);
		} else {
			//Oauth not there, but SM session is there
			try {	
				tokenRespons = restTemplate.getForEntity(String.format("http://localhost:8080/auth/oauth/check_token?token=%s", oAuthToken.substring(7)), String.class);
			} catch (Exception e) {
				System.out.println("PRE FILTER >>Check Token  Shows Invalid");
			}
			if (tokenRespons != null && tokenRespons.getStatusCode().is2xxSuccessful()) {
				System.out.println("PRE FILTER >>Token valid");
			} else {
				System.out.println("PRE FILTER >>Token Not Valid/expired, So renew it in API Gateway");
				oAuthToken = getTheToken(ctx,smUser,smUserGroups);
			}

		}
		
		 Map<String, String> map = new HashMap<String, String>();
	    
		 map.put("Authorization", oAuthToken);
		// map.put("SM_USER", smUser);
		// map.put("SM_USERGROUPS", smUserGroups);
			ctx.getZuulRequestHeaders().putAll(map); 
		return null;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public int filterOrder() {
		
		return 1;
	}

	@Override
	public String filterType() {
		
		return "pre";
	}
	
	
	private String getTheToken(RequestContext ctx, String smUser,String smUserGroups){
		String oAuthToken = null;
		/*Cookie[] cookies = ctx.getRequest().getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie c : cookies)
				System.out.println("PRE FILTER >>cookie name" + c.getName() + " cookie Value =" + c.getValue());
		}*/

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("roles", smUserGroups);
		httpHeaders.add("userName", smUser);
		HttpEntity<String> httpEntity = new HttpEntity<String>(null, httpHeaders);

		ResponseEntity<DefaultOAuth2AccessToken> response = null;
		try {
			response = restTemplate.exchange("http://localhost:8080/auth/getTokenForUser", HttpMethod.GET,
					httpEntity, DefaultOAuth2AccessToken.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		oAuthToken = response.getBody().getTokenType() + " " + response.getBody().getValue();
		System.out.println("PRE FILTER >>Token we got In API Gateway>>>= " + oAuthToken);
		return oAuthToken;
	}

}
