package com.example.zuul.filters;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.servlet.http.Cookie;

import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

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
		
		
		System.out.println("POST FILTER >>" + ctx.getZuulRequestHeaders().get("Authorization"));
		ctx.getResponse().addHeader("Authorization",ctx.getZuulRequestHeaders().get("Authorization"));
		ctx.getResponse().addHeader("TokenRenewed",ctx.getZuulRequestHeaders().get("TokenRenewed"));
	
		
		InputStream stream = ctx.getResponseDataStream();
		String body=null;
		try {
			body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		ctx.setResponseBody(body);
		
		System.out.println("ROUTE FILTER >>ResponseBody=" + ctx.getResponseBody());
		
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
