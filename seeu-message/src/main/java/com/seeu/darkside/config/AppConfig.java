package com.seeu.darkside.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Configuration
public class AppConfig {

	@Value("${google.firebase.security.serviceAccountPath}")
	private String serviceAccountPath;

	@Value("${google.firebase.messaging.url}")
	private String firebaseMessagingUrl;

	@Value("${google.firebase.database.url")
	private String firebaseDatabaseUrl;

	@PostConstruct
	public void initializeFirebaseMessaging() throws IOException {
		InputStream stream = new FileInputStream(serviceAccountPath);

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(stream))
				.setDatabaseUrl(firebaseDatabaseUrl)
				.build();

		FirebaseApp.initializeApp(options);
	}

	@Bean
	public GoogleCredential googleCredential() throws IOException {
		InputStream stream = new FileInputStream(serviceAccountPath);
		return GoogleCredential
				.fromStream(stream)
				.createScoped(Arrays.asList("https://www.googleapis.com/auth/firebase.messaging"));
	}

	@Bean
	public FirebaseConfig firebaseConfig() {
		return FirebaseConfig.builder()
				.url(firebaseMessagingUrl)
				.build();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}