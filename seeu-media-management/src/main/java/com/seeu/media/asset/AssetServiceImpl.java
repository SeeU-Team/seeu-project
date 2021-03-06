package com.seeu.media.asset;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.seeu.media.utils.GenerateFileUrl;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssetServiceImpl implements AssetService {

	private static final String BUCKET_SOURCE = "seeu-bucket";
	private static final String DARK_VALUE = "-dark.";
	private static final String LIGHT_VALUE = "-light.";

	private AssetRepository assetRepository;

	private AmazonS3 amazonS3;

	@Autowired
	public AssetServiceImpl(AssetRepository assetRepository, AmazonS3 amazonS3) {
		this.assetRepository = assetRepository;
		this.amazonS3 = amazonS3;
	}

	@Override
	public AssetEntity getAsset(Long assetId) {
		AssetEntity assetEntity = assetRepository.findOneById(assetId);

		return getAssetWithUrl(assetEntity);
	}

	@Override
	public List<AssetEntity> getAllAssets() {
		return assetRepository.findAll();
	}

	@Override
	public AssetEntity createAsset(MultipartFile imageDark, MultipartFile imageLight, String name) {
		String darkFileName = "";
		if (imageDark != null) {
			String file1extension = FilenameUtils.getExtension(imageDark.getOriginalFilename());
			darkFileName = name + DARK_VALUE + file1extension;
		}
		String file2extension = FilenameUtils.getExtension(imageLight.getOriginalFilename());
		String lightFileName = name + LIGHT_VALUE + file2extension;
		Date now = new Date();

		try {
			if (imageDark != null) {
				amazonS3.putObject(BUCKET_SOURCE, darkFileName, imageDark.getInputStream(), null);
			}
			amazonS3.putObject(BUCKET_SOURCE, lightFileName, imageLight.getInputStream(), null);
		} catch (IOException e) {
			e.printStackTrace();
		}

		AssetEntity entityToCreate = AssetEntity.builder()
				.name(name)
				.imageDark(darkFileName)
				.imageLight(lightFileName)
				.created(now)
				.updated(now)
				.build();

		return assetRepository.save(entityToCreate);
	}

	public void saveImage(String image, String fileName) {
		String file1extension = FilenameUtils.getExtension(fileName);
		String darkFileName = "FILEA-NAME" + file1extension;

		byte[] bytes = Base64.decodeBase64(image);
		InputStream inputStream = new ByteArrayInputStream(bytes);

		amazonS3.putObject(BUCKET_SOURCE, darkFileName, inputStream, null);
	}

	@Override
	public S3Object getImageDark(Long idAsset) {
		AssetEntity oneByIdAsset = assetRepository.findOneById(idAsset);
		if (oneByIdAsset == null)
			throw new AssetNotFoundException("Asset not Found Exception");
		S3Object s3Object = amazonS3.getObject(BUCKET_SOURCE, oneByIdAsset.getImageDark());

		return s3Object;
	}

	@Override
	public S3Object getImageLight(Long idAsset) {
		AssetEntity oneByIdAsset = assetRepository.findOneById(idAsset);
		if (oneByIdAsset == null)
			throw new AssetNotFoundException("Asset not Found Exception");
		S3Object s3Object = amazonS3.getObject(BUCKET_SOURCE, oneByIdAsset.getImageLight());
		return s3Object;
	}

	@Override
	public void updateImageDark(MultipartFile imageDark, Long idAsset) {
		AssetEntity entityToUpdate = assetRepository.findOneById(idAsset);
		if (entityToUpdate == null)
			throw new AssetNotFoundException("Asset not Found Exception");
		String imageName = entityToUpdate.getImageDark();
		try {
			amazonS3.putObject(BUCKET_SOURCE, imageName, imageDark.getInputStream(), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Date date = new Date();
		entityToUpdate.setUpdated(date);
		assetRepository.save(entityToUpdate);
	}

	@Override
	public void updateImageLight(MultipartFile imageLight, Long assetId) {
		AssetEntity entityToUpdate = assetRepository.findOneById(assetId);
		String imageName = entityToUpdate.getImageLight();
		try {
			amazonS3.putObject(BUCKET_SOURCE, imageName, imageLight.getInputStream(), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Date date = new Date();
		entityToUpdate.setUpdated(date);
		assetRepository.save(entityToUpdate);
	}

	@Override
	public List<AssetEntity> getAssetsWithUrls() {
		return getAllAssets().stream()
				.map(this::getAssetWithUrl)
				.collect(Collectors.toList());
	}

	@Override
	public void deleteAsset(Long idAsset) {
		assetRepository.deleteById(idAsset);
	}

	private AssetEntity getAssetWithUrl(AssetEntity asset) {
		if (asset.getImageDark() != null) {
			URL urlDark = GenerateFileUrl.generateUrlFromFile(amazonS3, BUCKET_SOURCE, asset.getImageDark());
			asset.setImageDark(urlDark.toExternalForm());
		}

		URL urlLight = GenerateFileUrl.generateUrlFromFile(amazonS3, BUCKET_SOURCE, asset.getImageLight());
		asset.setImageLight(urlLight.toExternalForm());

		return asset;
	}
}