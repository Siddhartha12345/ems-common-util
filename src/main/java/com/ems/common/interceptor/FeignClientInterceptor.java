package com.ems.common.interceptor;

import com.ems.common.constant.EmsCommonConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeignClientInterceptor implements RequestInterceptor {

    private final HttpServletRequest httpServletRequest;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String jwtToken = httpServletRequest.getHeader(EmsCommonConstant.AUTHORIZATION_HEADER);
        requestTemplate.header(EmsCommonConstant.AUTHORIZATION_HEADER, jwtToken);
    }
}