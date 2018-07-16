package com.seeu.media.utils;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import java.net.URL;
import java.util.Date;

public class GenerateFileUrl {

	public static URL generateUrlFromFile(AmazonS3 amazonS3, String bucketName, String fileName) {
		if (fileName == null) {
			return null;
		}
		Date expiration = new Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 60 * 60;
		expiration.setTime(expTimeMillis);

		GeneratePresignedUrlRequest generatePresignedUrlRequest =
				new GeneratePresignedUrlRequest(bucketName, fileName)
						.withMethod(HttpMethod.GET)
						.withExpiration(expiration);
		URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
		return url;
	}
}
