package com.fon.master.api_gateway.filter;

import com.fon.master.api_gateway.utils.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.server.mvc.filter.FormFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

@Component
public class AuthenticationFilter implements Filter, Ordered {
    public static final List<String> publicApiEndpoints = List.of(
            "/auth/login",
            "/auth/register",
            "/trips"
    );

    public static final List<String> publicMethods = List.of(
            "GET",
            "OPTIONS"
    );

    @Autowired
    public JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // check if request method is GET or URI is in public endpoints
        if (publicMethods.contains(request.getMethod()) || publicApiEndpoints.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // extract JWT from request header
        String token = getJWTFromRequest(request);

        // check if token is empty
        if (!StringUtils.hasText(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // validate token
        try {
            jwtUtil.validateToken(token);

            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    @Override
    public int getOrder() {
        return FormFilter.HIGHEST_PRECEDENCE;
    }
}
