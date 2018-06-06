package com.seeu.media.rs;

import com.seeu.media.asset.AssetEntity;
import com.seeu.media.asset.AssetService;
import com.seeu.media.rs.dto.AssetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/assets")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public AssetEntity createNewAsset(@RequestBody AssetDTO assetDTO) {
        return assetService.createAsset(assetDTO);
    }

    @GetMapping
    public AssetEntity getTagInfo(@RequestParam Long assetId) {
        return assetService.getAsset(assetId);
    }

    /**
     * DEBUG
     * @return
     */
    @GetMapping("/list")
    public List<AssetEntity> listAssets() {
        return assetService.getAllAssets();
    }

}
