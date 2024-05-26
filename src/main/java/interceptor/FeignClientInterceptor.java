package interceptor;

import com.ems.common.constant.EmsCommonConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            String jwtToken = requestAttributes.getRequest().getHeader(EmsCommonConstant.AUTHORIZATION_HEADER);
            requestTemplate.header(EmsCommonConstant.AUTHORIZATION_HEADER, jwtToken);
        }
    }
}