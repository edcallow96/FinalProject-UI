package com.finalproject.frontend;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
@Getter
@Setter
public class ApplicationProperties {

  private String awsRegion;
}
