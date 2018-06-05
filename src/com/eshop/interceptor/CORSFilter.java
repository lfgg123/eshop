package com.eshop.interceptor;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;


public class CORSFilter implements Filter {
public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    HttpServletResponse response = (HttpServletResponse) res;
//    HttpServletRequest request=(HttpServletRequest) req;
//    Enumeration headerNames = request.getHeaderNames();
//    Map<String, String> map = new HashMap<String, String>();
//    while (headerNames.hasMoreElements()) {
//        String key = (String) headerNames.nextElement();
//        String value = request.getHeader(key);
//        map.put(key, value);
//      }
//    String or=request.getHeader("origin");
    response.setHeader("Access-Control-Allow-Origin",  "*");
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Credentials","true");
    response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    
    chain.doFilter(req, res);
}
public void init(FilterConfig filterConfig) {}
public void destroy() {}
}