package com.example.demo.repository;

import com.example.demo.model.entity.RepositoryDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryDetailsRepository extends JpaRepository<RepositoryDetails, Long> {
    RepositoryDetails findByOwnerAndRepositoryName(String owner, String repositoryName);

}
