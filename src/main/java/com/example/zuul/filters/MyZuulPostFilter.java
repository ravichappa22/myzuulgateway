package com.example.zuul.filters;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class MyZuulPostFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		//Map<String, String> map = new HashMap<String, String>();
		//map.put("Authorization", token);
	//	map.put("SM_USER", smUser);
		//map.put("SM_USERGROUPS", smUserGroups);
		System.out.println("In Route Filter" + ctx.getZuulRequestHeaders().get("Authorization"));
		ctx.getResponse().addHeader("Authorization",ctx.getZuulRequestHeaders().get("Authorization"));
		

		InputStream stream = ctx.getResponseDataStream();
		String body=null;
		try {
			body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ctx.setResponseBody("Modified via setResponseBody(): " + body);
		
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
