package com.example.demo.service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.mapper.RepositoryDetailsMapper;
import com.example.demo.model.dto.GitHubDataDto;
import com.example.demo.model.dto.RepositoryDetailsDTO;
import com.example.demo.model.entity.RepositoryDetails;
import com.example.demo.repository.RepositoryDetailsRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RepositoryDetailsService {
    private final RepositoryDetailsRepository repositoryDetailsRepository;
    private final GitHubDataService gitHubDataService;
    private final RepositoryDetailsMapper repositoryDetailsMapper;

    @Transactional
    public RepositoryDetailsDTO saveRepositoryDetails(String owner, String repositoryName) {
        GitHubDataDto gitHubData = gitHubDataService.getRepositoryDetails(owner, repositoryName);
        Optional<RepositoryDetails> existingRepository = repositoryDetailsRepository.findByOwnerAndRepositoryName(owner, repositoryName);
        if (existingRepository.isPresent()) {
            throw new BadRequestException("Repository with name " + repositoryName + " already exists for owner " + owner);
        }

        RepositoryDetailsDTO repositoryDetailsDTO = new RepositoryDetailsDTO(
                owner,
                repositoryName,
                gitHubData.getDescription(),
                gitHubData.getClone_url(),
                gitHubData.getStars(),
                gitHubData.getCreated_at()
        );

        RepositoryDetails savedEntity = repositoryDetailsRepository.save(repositoryDetailsMapper.dtoToEntity(repositoryDetailsDTO));
        return repositoryDetailsMapper.entityToDto(savedEntity);
    }

    public Optional<RepositoryDetailsDTO> getRepositoryDetails(String owner, String repositoryName) {
        log.info("Getting repository details for owner: {} and repository name: {}", owner, repositoryName);
        Optional<RepositoryDetails> entityOptional = repositoryDetailsRepository.findByOwnerAndRepositoryName(owner, repositoryName);

        return entityOptional.map(repositoryDetailsMapper::entityToDto);
    }

    @Transactional
    public void deleteRepository(String owner, String repositoryName) {
        log.info("Deleting repository for owner: {} and repository name: {}", owner, repositoryName);
        Optional<RepositoryDetails> existingRepository = repositoryDetailsRepository.findByOwnerAndRepositoryName(owner, repositoryName);

        existingRepository.ifPresentOrElse(repositoryDetailsRepository::delete, () -> {
            throw new BadRequestException("Repository not found for owner: " + owner + " and repository name: " + repositoryName);
        });
    }

    @Transactional
    public RepositoryDetailsDTO updateRepositoryDetails(String owner, String repositoryName, RepositoryDetailsDTO updatedDetails) {
        log.info("Updating repository details for owner: {} and repository name: {}", owner, repositoryName);

        RepositoryDetails existingRepository = repositoryDetailsRepository.findByOwnerAndRepositoryName(owner, repositoryName)
                .orElseThrow(() -> new BadRequestException("Repository not found for owner: " + owner + " and repository name: " + repositoryName));

        existingRepository.setDescription(updatedDetails.getDescription());
        existingRepository.setCloneUrl(updatedDetails.getCloneUrl());
        existingRepository.setStars(updatedDetails.getStars());
        existingRepository.setCreatedAt(updatedDetails.getCreatedAt());

        RepositoryDetails updatedRepository = repositoryDetailsRepository.save(existingRepository);
        return repositoryDetailsMapper.entityToDto(updatedRepository);
    }
}