package com.example.galleryfilter.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class TwitterAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    @Override
    protected String obtainUsername(HttpServletRequest request) {
        // TODO Auto-generated method stub
        return super.obtainUsername(request);
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        // TODO Auto-generated method stub
        return super.obtainPassword(request);
    }
}
