package com.example.zuul.filters;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.servlet.http.Cookie;

import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.netflix.util.Pair;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class MyZuulPostFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		return RequestContext.getCurrentContext().sendZuulResponse();
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		
		
		System.out.println("In POST Filter" + ctx.getZuulRequestHeaders().get("Authorization"));
		ctx.getResponse().addHeader("Authorization",ctx.getZuulRequestHeaders().get("Authorization"));
		ctx.getResponse().addHeader("SM_USER",ctx.getZuulRequestHeaders().get("SM_USER"));
		ctx.getResponse().addHeader("SM_USERGROUPS",ctx.getZuulRequestHeaders().get("SM_USERGROUPS"));
		//Cookie c = new Cookie("SMSESSION", "JESSIONID");
		//c.setMaxAge(2);
		Cookie[] cookies = ctx.getRequest().getCookies();
		if(cookies != null && cookies.length>0){
			for(Cookie cookie : cookies){
			System.out.println("adding cookie in post filter");
			cookie.setMaxAge(2);
			ctx.getResponse().addCookie(cookie);
			}
		}
		
		//}
		
		InputStream stream = ctx.getResponseDataStream();
		String body=null;
		try {
			body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ctx.setResponseBody(body);
		
		System.out.println("ResponseBody=" + ctx.getResponseBody());
		
		return null;
	}

	@Override
	public String filterType() {
		
		return "post";
	}

	@Override
	public int filterOrder() {
		
		return 0;
	}

}
