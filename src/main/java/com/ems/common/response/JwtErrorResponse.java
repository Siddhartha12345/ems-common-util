package com.ems.common.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtErrorResponse {

    private String errorCode;

    private String errorMessage;
}
