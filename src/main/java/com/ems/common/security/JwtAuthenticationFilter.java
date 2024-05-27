package com.ems.common.security;

import com.ems.common.constant.AuthenticationErrorEnum;
import com.ems.common.constant.EmsCommonConstant;
import com.ems.common.entity.User;
import com.ems.common.exception.UserAccessDeniedException;
import com.ems.common.feign.AuthenticationFeign;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;

    private final AuthenticationFeign authenticationFeign;

    private final JwtHelper jwtHelper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) {

        final String authHeader = request.getHeader(EmsCommonConstant.AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(EmsCommonConstant.TYPE_BEARER)) {
            handlerExceptionResolver.resolveException(request, response, null,
                    new UserAccessDeniedException(AuthenticationErrorEnum.ACCESS_DENIED.getErrorCode(), AuthenticationErrorEnum.ACCESS_DENIED.getErrorMessage()));
            return;
        }
        try {
            final String jwt = authHeader.substring(7);
            final String userEmail = jwtHelper.extractUsername(jwt);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (userEmail != null && authentication == null) {
                User userDetails = authenticationFeign.loadUserByUsername(userEmail).getBody();
                if (jwtHelper.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}
