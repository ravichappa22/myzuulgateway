package com.example.zuul.hystrixfallback;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;

@Configuration
public class ZuulFallbackProviderDataRoute {
	
	@Bean
	public ZuulFallbackProvider zuulFallbackProvider() {
	        return new ZuulFallbackProvider() {
	            @Override
	            public String getRoute() {
	                return "data";
	            }
	            
	            @Override
	            public ClientHttpResponse fallbackResponse() {
	                return new ClientHttpResponse() {
	                    @Override
	                    public HttpStatus getStatusCode() throws IOException {
	                        return HttpStatus.OK;
	                    }
	                    @Override
	                    public int getRawStatusCode() throws IOException {
	                        return 200;
	                    }
	 
	 
	                    @Override
	                    public String getStatusText() throws IOException {
	                        return "OK";
	                    }
	                   @Override
	                    public void close() {
	                    }
	                    @Override
	                    public InputStream getBody() throws IOException {
	                        return new ByteArrayInputStream("System Down, Pleas try later".getBytes());
	                    }
	                    @Override
	                    public HttpHeaders getHeaders() {
	                        HttpHeaders headers = new HttpHeaders();
	                        headers.setContentType(MediaType.APPLICATION_JSON);
	                        return headers;
	                    }
	                };
	            }
	        };
	    }
	
	
	@Bean
	public ZuulFallbackProvider zuulFallbackProviderFordata2() {
	        return new ZuulFallbackProvider() {
	            @Override
	            public String getRoute() {
	                return "data2";
	            }
	            
	            @Override
	            public ClientHttpResponse fallbackResponse() {
	                return new ClientHttpResponse() {
	                    @Override
	                    public HttpStatus getStatusCode() throws IOException {
	                        return HttpStatus.OK;
	                    }
	                    @Override
	                    public int getRawStatusCode() throws IOException {
	                        return 200;
	                    }
	 
	 
	                    @Override
	                    public String getStatusText() throws IOException {
	                        return "OK";
	                    }
	                   @Override
	                    public void close() {
	                    }
	                    @Override
	                    public InputStream getBody() throws IOException {
	                        return new ByteArrayInputStream("System Down, Pleas try later for data2".getBytes());
	                    }
	                    @Override
	                    public HttpHeaders getHeaders() {
	                        HttpHeaders headers = new HttpHeaders();
	                        headers.setContentType(MediaType.APPLICATION_JSON);
	                        return headers;
	                    }
	                };
	            }
	        };
	    }

}
