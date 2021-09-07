package com.backend.repository;

import com.backend.model.Secret;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@EnableScan
public interface SecretRepository extends CrudRepository<Secret, String> {

    public Optional<Secret> findById(String id);
    public List<Secret> findByUserId(String userId);
    public void deleteById(String id);

}
