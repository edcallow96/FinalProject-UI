package com.finalproject.frontend.repository;

import com.finalproject.frontend.model.User;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface UserRepository extends CrudRepository<User, String> {
}
