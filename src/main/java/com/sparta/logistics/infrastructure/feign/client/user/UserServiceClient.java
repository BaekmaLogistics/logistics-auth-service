package com.sparta.logistics.infrastructure.feign.client.user;

import com.sparta.logistics.infrastructure.feign.dto.request.CreatePendingUserRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserServiceClient {
  @PostMapping("/internal/api/v1/users/signup")
  void createPendingUser(
      @RequestBody CreatePendingUserRequest request
  );
}
