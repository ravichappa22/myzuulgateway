package com.example.zuul.filters;

import java.util.UUID;

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
		System.out.println("ROUTE FILTER >>Routing to Service");
		UUID correlationId = UUID.randomUUID();
		RequestContext.getCurrentContext().getZuulRequestHeaders().put("X-CorrelationId", correlationId.toString());
		System.out.println("ROUTE FILTER >>Request Headers while Calling Backed Service" + RequestContext.getCurrentContext().getZuulRequestHeaders());
		
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
