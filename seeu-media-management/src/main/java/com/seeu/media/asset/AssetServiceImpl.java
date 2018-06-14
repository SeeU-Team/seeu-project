package com.seeu.media.asset;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {

    private static final String BUCKET_SOURCE = "seeu-bucket";
    private AssetRepository assetRepository;

    private AmazonS3 amazonS3;

    @Autowired
    public AssetServiceImpl(AssetRepository assetRepository, AmazonS3 amazonS3) {
        this.assetRepository = assetRepository;
        this.amazonS3 = amazonS3;
    }

    /**@Override
    public AssetEntity createAsset(AssetDTO assetDTO) {
        Date now = new Date();

        AssetEntity entityToCreate = AssetEntity.builder()
                .name(assetDTO.getName())
                .mediaId(assetDTO.getMediaId())
                .created(now)
                .updated(now)
                .build();

        return assetRepository.save(entityToCreate);
    }**/

    @Override
    public AssetEntity getAsset(Long assetId) {
        return assetRepository.findOneByIdAsset(assetId);
    }

    @Override
    public List<AssetEntity> getAllAssets() {
        return assetRepository.findAll();
    }

    @Override
    public AssetEntity createAsset(MultipartFile file1, MultipartFile file2, String name, int mediaId) {
        try {
            amazonS3.putObject(BUCKET_SOURCE, file1.getOriginalFilename(), file1.getInputStream(), null);
            amazonS3.putObject(BUCKET_SOURCE, file2.getOriginalFilename(), file2.getInputStream(), null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Date now = new Date();

        AssetEntity entityToCreate = AssetEntity.builder()
                .name(name)
                .imageDark(file1.getOriginalFilename())
                .imageLight(file2.getOriginalFilename())
                .created(now)
                .updated(now)
                .build();

        return assetRepository.save(entityToCreate);
    }

    @Override
    public S3Object getImageDark(Long idAsset) throws IOException {
        AssetEntity oneByIdAsset = assetRepository.findOneByIdAsset(idAsset);
        S3Object s3Object = amazonS3.getObject(BUCKET_SOURCE, oneByIdAsset.getImageDark());

        return s3Object;
    }

    @Override
    public S3Object getImageLight(Long idAsset) throws IOException {
        AssetEntity oneByIdAsset = assetRepository.findOneByIdAsset(idAsset);
        S3Object s3Object = amazonS3.getObject(BUCKET_SOURCE, oneByIdAsset.getImageLight());

        return s3Object;
    }
}
