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

    @PostMapping(value = "/file", consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity createAsset(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2, @RequestParam("name") String name, @RequestParam("mediaId") int mediaId) {

        if (file1 == null || file2 == null)
            throw new AssetFileIsNullException("Asset file 1 or 2 is null");
        if (name == null)
            throw new AssetNameIsNullException("Asset name is null");

        assetService.createAsset(file1, file2, name, mediaId);

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
