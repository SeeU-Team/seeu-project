package com.seeu.media.asset;

import com.seeu.media.rs.dto.AssetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {

    private AssetRepository assetRepository;

    @Autowired
    public AssetServiceImpl(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Override
    public AssetEntity createAsset(AssetDTO assetDTO) {
        Date now = new Date();

        AssetEntity entityToCreate = AssetEntity.builder()
                .name(assetDTO.getName())
                .mediaId(assetDTO.getMediaId())
                .created(now)
                .updated(now)
                .build();

        return assetRepository.save(entityToCreate);
    }

    @Override
    public AssetEntity getAsset(Long assetId) {
        return assetRepository.findOneByIdAsset(assetId);
    }

    @Override
    public List<AssetEntity> getAllAssets() {
        return assetRepository.findAll();
    }
}
