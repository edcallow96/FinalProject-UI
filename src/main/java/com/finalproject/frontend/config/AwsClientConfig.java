package com.finalproject.frontend.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.finalproject.frontend.ApplicationProperties;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableDynamoDBRepositories("com.finalproject")
public class AwsClientConfig {

  private final ApplicationProperties applicationProperties;

  public AwsClientConfig(final ApplicationProperties applicationProperties) {
    this.applicationProperties = applicationProperties;
  }

  @Bean
  @Profile("default")
  public AmazonS3 awsS3Client() {
    return AmazonS3ClientBuilder.standard().withRegion(applicationProperties.getAwsRegion()).build();
  }

  @Bean
  @Profile("default")
  public AmazonDynamoDB amazonDynamoDB() {
    return AmazonDynamoDBClientBuilder.standard().build();
  }

}