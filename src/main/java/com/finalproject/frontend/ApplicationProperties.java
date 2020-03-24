package com.finalproject.frontend;

import lombok.Getter;
import lombok.Setter;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
@Getter
@Setter
public class ApplicationProperties {

  private String awsRegion;
  private String awsAccessKey;
  private String awsSecretKey;
}
