package com.seeu.media.rs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Base64InputStream;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetDto {

    private String nameFile;

    private String base64;

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}
