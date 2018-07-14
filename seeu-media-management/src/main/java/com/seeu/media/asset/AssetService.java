package com.seeu.media.asset;

import com.amazonaws.services.s3.model.S3Object;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface AssetService {
    AssetEntity getAsset(Long assetId);

    List<AssetEntity> getAllAssets();

    AssetEntity createAsset(MultipartFile file1, MultipartFile file2, String name);

    S3Object getImageDark(Long idAsset);

    S3Object getImageLight(Long idAsset);

    void updateImageDark(MultipartFile imageDark, Long idAsset);

    void updateImageLight(MultipartFile imageLight, Long assetId);

	List<AssetEntity> getAssetsWithUrls();

    void deleteAsset(Long idAsset);
}
