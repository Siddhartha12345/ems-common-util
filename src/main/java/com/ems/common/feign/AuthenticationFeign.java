package com.ems.common.feign;

import com.ems.common.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-feign-svc", url = "${feign.auth-feign-svc.baseUrl}${feign.auth-feign-svc.baseContext}")
public interface AuthenticationFeign {

    @GetMapping("/load-user/{username}")
    ResponseEntity<User> loadUserByUsername(@PathVariable String username);
}
