package com.example.zuul.filters;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class MyZuulErrorFilter extends ZuulFilter {

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();    // Populate context with new response values
       		System.out.println("ERROR FILTER --> In Error Filter");
    	   		ctx.setResponseBody("we are having trouble serving your request, please try again later");
             ctx.getResponse().setContentType("application/json");
             ctx.setResponseStatusCode(500);
             ctx.setSendZuulResponse(true);
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
		
		return "error";
	}

}
