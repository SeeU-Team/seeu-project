package com.seeu.media.rs;

import com.amazonaws.services.s3.model.S3Object;
import com.seeu.media.asset.AssetEntity;
import com.seeu.media.asset.AssetService;
import com.seeu.media.rs.exception.AssetFileIsNullException;
import com.seeu.media.rs.exception.AssetNameIsNullException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/assets")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity createAsset(@RequestParam("imageDark") MultipartFile imageDark, @RequestParam("imageLight") MultipartFile imageLight, @RequestParam("name") String name) {

        if (imageDark == null || imageLight == null)
            throw new AssetFileIsNullException("Asset file 1 or 2 is null");
        if (name == null)
            throw new AssetNameIsNullException("Asset name is null");

        assetService.createAsset(imageDark, imageLight, name);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping
    public AssetEntity getTagInfo(@RequestParam Long assetId) {
        return assetService.getAsset(assetId);
    }

    @GetMapping("/dark")
    public void getDarkImageById(@RequestParam Long assetId, HttpServletResponse response) throws IOException {
        S3Object s3Object = assetService.getImageDark(assetId);
        InputStream inputStream = s3Object.getObjectContent();
        response.setHeader("Content-Disposition", "attachment; filename=" + s3Object.getKey());
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }

    @GetMapping("/light")
    public void getLightImageById(@RequestParam Long assetId, HttpServletResponse response) throws IOException {
        S3Object s3Object = assetService.getImageLight(assetId);
        InputStream inputStream = s3Object.getObjectContent();
        response.setHeader("Content-Disposition", "attachment; filename=" + s3Object.getKey());
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }

    @PutMapping(value = "/dark", consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity updateDarkImageAsset(@RequestParam("imageDark") MultipartFile imageDark, @RequestParam("assetId") Long assetId) {

        if (imageDark == null )
            throw new AssetFileIsNullException("Asset file is null");
        if (assetId == null)
            throw new AssetNameIsNullException("assetId is null");

        assetService.updateImageDark(imageDark, assetId);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping(value = "/light", consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity updateLightImageAsset(@RequestParam("imageLight") MultipartFile imageLight, @RequestParam("assetId") Long assetId) {

        if (imageLight == null )
            throw new AssetFileIsNullException("Asset file is null");
        if (assetId == null)
            throw new AssetNameIsNullException("assetId is null");

        assetService.updateImageLight(imageLight, assetId);

        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * DEBUG
     *
     * @return
     */
    @GetMapping("/list")
    public List<AssetEntity> listAssets() {
        return assetService.getAllAssets();
    }

}
