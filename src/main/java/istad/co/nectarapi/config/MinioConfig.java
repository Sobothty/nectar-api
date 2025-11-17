package istad.co.nectarapi.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MinioConfig {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Bean
    public MinioClient minioClient() {
        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();

            // Initialize bucket in a separate thread to avoid blocking startup
            initializeBucketAsync(minioClient);

            return minioClient;
        } catch (Exception e) {
            log.error("Error creating MinIO client: {}", e.getMessage());
            throw new RuntimeException("Failed to initialize MinIO client", e);
        }
    }

    private void initializeBucketAsync(MinioClient minioClient) {
        new Thread(() -> {
            try {
                // Create bucket if it doesn't exist
                boolean bucketExists = minioClient.bucketExists(
                        BucketExistsArgs.builder()
                                .bucket(bucketName)
                                .build()
                );

                if (!bucketExists) {
                    minioClient.makeBucket(
                            MakeBucketArgs.builder()
                                    .bucket(bucketName)
                                    .build()
                    );
                    log.info("MinIO bucket created: {}", bucketName);

                    // Set bucket policy to public read
                    String policy = """
                            {
                                "Version": "2012-10-17",
                                "Statement": [
                                    {
                                        "Effect": "Allow",
                                        "Principal": {"AWS": "*"},
                                        "Action": ["s3:GetObject"],
                                        "Resource": ["arn:aws:s3:::%s/*"]
                                    }
                                ]
                            }
                            """.formatted(bucketName);

                    minioClient.setBucketPolicy(
                            SetBucketPolicyArgs.builder()
                                    .bucket(bucketName)
                                    .config(policy)
                                    .build()
                    );
                    log.info("MinIO bucket policy set to public read");
                } else {
                    log.info("MinIO bucket already exists: {}", bucketName);
                }
            } catch (Exception e) {
                log.warn("MinIO server is not available. Bucket initialization will be skipped. Error: {}", e.getMessage());
            }
        }).start();
    }
}