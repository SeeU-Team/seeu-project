package com.seeu.media.rs;

import com.amazonaws.services.s3.model.S3Object;
import com.seeu.media.asset.AssetEntity;
import com.seeu.media.asset.AssetService;
import com.seeu.media.rs.dto.AssetDto;
import com.seeu.media.rs.exception.AssetFileIsNullException;
import com.seeu.media.rs.exception.AssetNameIsNullException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
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
@RequestMapping("/medias/assets")
public class AssetController {

    private final AssetService assetService;

    @Autowired
    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public void createAsset(@RequestParam("imageDark") MultipartFile imageDark, @RequestParam("imageLight") MultipartFile imageLight, @RequestParam("name") String name) {

        if (imageLight == null)
            throw new AssetFileIsNullException("Asset file 1 or 2 is null");
        if (name == null)
            throw new AssetNameIsNullException("Asset name is null");

        assetService.createAsset(imageDark, imageLight, name);
    }

    @GetMapping
    public AssetEntity getAssetInfo(@RequestParam Long assetId) {
        return assetService.getAsset(assetId);
    }

    @GetMapping("/all")
    public List <AssetEntity> getAssetInfoWithImage() {
        return assetService.getAssetsWithUrls();
    }


    @GetMapping("/dark")
    public AssetDto getDarkImageByIdJson(@RequestParam Long assetId, HttpServletResponse response) throws IOException {
        S3Object s3Object = assetService.getImageDark(assetId);
        InputStream inputStream = s3Object.getObjectContent();
        byte[] image = IOUtils.toByteArray(inputStream);
        String base64String = Base64.encodeBase64String(image);
        AssetDto assetDto = new AssetDto(s3Object.getKey(), base64String);
        return assetDto;
    }

    @GetMapping("/light")
    public AssetDto getLightImageByIdJson(@RequestParam Long assetId, HttpServletResponse response) throws IOException {
        S3Object s3Object = assetService.getImageLight(assetId);
        InputStream inputStream = s3Object.getObjectContent();
        byte[] image = IOUtils.toByteArray(inputStream);
        String base64String = Base64.encodeBase64String(image);
        AssetDto assetDto = new AssetDto(s3Object.getKey(), base64String);
        return assetDto;
    }

    @GetMapping("/dark/download")
    public void getDarkImageById(@RequestParam Long assetId, HttpServletResponse response) throws IOException {
        S3Object s3Object = assetService.getImageDark(assetId);
        InputStream inputStream = s3Object.getObjectContent();
        response.setHeader("Content-Disposition", "attachment; filename=" + s3Object.getKey());
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }

    @GetMapping("/light/download")
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
