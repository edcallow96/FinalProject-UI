package com.finalproject.frontend;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.s3.AmazonS3;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
class FrontEndApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@MockBean
	private AmazonS3 amazonS3;

	@MockBean
	private AmazonDynamoDB amazonDynamoDB;

	@Test
	void contextLoads() {
		assertThat(applicationContext, notNullValue());
	}

}
