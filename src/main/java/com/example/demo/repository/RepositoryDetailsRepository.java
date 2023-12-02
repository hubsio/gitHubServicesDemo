package com.example.demo.repository;

import com.example.demo.model.entity.RepositoryDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositoryDetailsRepository extends JpaRepository<RepositoryDetails, Long> {
    Optional<RepositoryDetails> findByOwnerAndRepositoryName(String owner, String repositoryName);

}
