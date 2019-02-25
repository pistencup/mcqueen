package com.github.pistencup.mcqueen.racetrack;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@ConditionalOnProperty(value = "mcqueen.racetrack.enable-request-wrapper", matchIfMissing = true)
@WebFilter
public class McqueenRequestFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        if(servletRequest instanceof HttpServletRequest) {
            requestWrapper = new McqueenServletRequestWrapper((HttpServletRequest) servletRequest);
        }
        if(null == requestWrapper) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            filterChain.doFilter(requestWrapper, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
