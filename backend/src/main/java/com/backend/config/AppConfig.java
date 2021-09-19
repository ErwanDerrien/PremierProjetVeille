package com.backend.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class AppConfig {

    @Value("${amazon.aws.region}")
    private String region;

    @Value("${amazon.aws.accesskey}")
    private String accessKey;

    @Value("${amazon.aws.secretkey}")
    private String secretKey;

    @Value("${amazon.aws.endpoint:http://localhost:8000}")
    private String endpoint;

    @Bean
    public DynamoDbClient getDynamoDbClient() throws URISyntaxException {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);

        return DynamoDbClient.builder().endpointOverride(new URI(endpoint)).region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds)).build();
    }

    @Bean
    public DynamoDbEnhancedClient getDynamoDbEnhancedClient() throws URISyntaxException {
        return DynamoDbEnhancedClient.builder().dynamoDbClient(getDynamoDbClient()).build();
    }
}
