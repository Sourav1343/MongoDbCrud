package com.mongodbcrudpoc.code.repository;


import com.mongodbcrudpoc.code.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
