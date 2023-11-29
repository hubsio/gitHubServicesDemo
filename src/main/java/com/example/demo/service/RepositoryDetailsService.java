package com.example.demo.service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.mapper.RepositoryDetailsMapper;
import com.example.demo.model.dto.GitHubDataDto;
import com.example.demo.model.dto.RepositoryDetailsDTO;
import com.example.demo.model.entity.RepositoryDetails;
import com.example.demo.repository.RepositoryDetailsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RepositoryDetailsService {
    private final RepositoryDetailsRepository repositoryDetailsRepository;
    private final GitHubDataService gitHubDataService;
    private final RepositoryDetailsMapper repositoryDetailsMapper;

    public RepositoryDetailsDTO saveRepositoryDetails(String owner, String repositoryName) {
        GitHubDataDto gitHubData = gitHubDataService.getRepositoryDetails(owner, repositoryName);
        RepositoryDetails existingRepository = repositoryDetailsRepository.findByOwnerAndRepositoryName(owner, repositoryName);

        if (existingRepository != null) {
            throw new BadRequestException("Repository with name " + repositoryName + " already exists for owner " + owner);
        }

        RepositoryDetailsDTO repositoryDetailsDTO = new RepositoryDetailsDTO();
        repositoryDetailsDTO.setOwner(owner);
        repositoryDetailsDTO.setRepositoryName(repositoryName);
        repositoryDetailsDTO.setDescription(gitHubData.getDescription());
        repositoryDetailsDTO.setCloneUrl(gitHubData.getClone_url());
        repositoryDetailsDTO.setStars(gitHubData.getStars());
        repositoryDetailsDTO.setCreatedAt(gitHubData.getCreated_at());

        RepositoryDetails savedEntity = repositoryDetailsRepository.save(repositoryDetailsMapper.dtoToEntity(repositoryDetailsDTO));

        return repositoryDetailsMapper.entityToDto(savedEntity);
    }

    public RepositoryDetailsDTO getRepositoryDetails(String owner, String repositoryName) {
        RepositoryDetails entity = repositoryDetailsRepository.findByOwnerAndRepositoryName(owner, repositoryName);

        if (entity != null) {
            return repositoryDetailsMapper.entityToDto(entity);
        } else {
            throw new BadRequestException("Repository details not found for owner: " + owner + " and repository name: " + repositoryName);
        }
    }

    public void deleteRepository(String owner, String repositoryName) {
        RepositoryDetails existingRepository = repositoryDetailsRepository.findByOwnerAndRepositoryName(owner, repositoryName);

        if (existingRepository == null) {
            throw new BadRequestException("Repository not found for owner: " + owner + " and repository name: " + repositoryName);
        }

        repositoryDetailsRepository.delete(existingRepository);
    }

    public RepositoryDetailsDTO updateRepositoryDetails(String owner, String repositoryName, RepositoryDetailsDTO updatedDetails) {
        RepositoryDetails existingRepository = repositoryDetailsRepository.findByOwnerAndRepositoryName(owner, repositoryName);

        if (existingRepository == null) {
            throw new EntityNotFoundException("Repository not found for owner: " + owner + " and repository name: " + repositoryName);
        }

        existingRepository.setDescription(updatedDetails.getDescription());
        existingRepository.setCloneUrl(updatedDetails.getCloneUrl());
        existingRepository.setStars(updatedDetails.getStars());
        existingRepository.setCreatedAt(updatedDetails.getCreatedAt());

        RepositoryDetails updatedRepository = repositoryDetailsRepository.save(existingRepository);
        return repositoryDetailsMapper.entityToDto(updatedRepository);
    }
}
