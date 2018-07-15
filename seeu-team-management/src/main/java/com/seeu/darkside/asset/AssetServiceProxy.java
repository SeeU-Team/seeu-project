package com.seeu.darkside.asset;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("seeu-media-management")
public interface AssetServiceProxy {

	@GetMapping("/medias/assets/{id}")
	AssetEntity getAssetInfo(@PathVariable("id") Long assetId);
}
