package com.example.zuul.filters;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class MyZuulRouteFilter extends ZuulFilter {


	@Override
	public boolean shouldFilter() {
		return RequestContext.getCurrentContext().sendZuulResponse();
	}

	@Override
	public Object run() {
		System.out.println("Routing to Service");
		System.out.println("Request Headers" + RequestContext.getCurrentContext().getZuulRequestHeaders());
		
		return null;
	}

	@Override
	public int filterOrder() {
		
		return 1;
	}

	@Override
	public String filterType() {
		
		return "route";
	}

}
