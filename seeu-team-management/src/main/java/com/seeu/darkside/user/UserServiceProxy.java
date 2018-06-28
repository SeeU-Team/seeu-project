package com.seeu.darkside.user;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("seeu-media-management")
public interface UserServiceProxy {
}
