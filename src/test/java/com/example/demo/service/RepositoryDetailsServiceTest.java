package com.example.demo.service;

import com.example.demo.mapper.RepositoryDetailsMapper;
import com.example.demo.model.dto.GitHubDataDto;
import com.example.demo.model.dto.RepositoryDetailsDTO;
import com.example.demo.model.entity.RepositoryDetails;
import com.example.demo.repository.RepositoryDetailsRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RepositoryDetailsServiceTest {
    RepositoryDetailsService repositoryDetailsService;
    RepositoryDetailsRepository repositoryDetailsRepository;
    RepositoryDetailsMapper repositoryDetailsMapper;
    GitHubDataService gitHubDataService;
    @BeforeEach
    void setUp() {
        this.repositoryDetailsRepository = mock(RepositoryDetailsRepository.class);
        this.repositoryDetailsMapper = mock(RepositoryDetailsMapper.class);
        this.gitHubDataService = mock(GitHubDataService.class);
        this.repositoryDetailsService = new RepositoryDetailsService(repositoryDetailsRepository, gitHubDataService, repositoryDetailsMapper);
    }

    @Test
    public void saveRepositoryDetails_WhenRepositoryDoesNotExist_ReturnsSavedDTO() {
        String owner = "owner";
        String repositoryName = "repo";
        LocalDateTime createdAt = LocalDateTime.now();

        GitHubDataDto gitHubData = new GitHubDataDto("fullName", "description", "cloneUrl", 5, createdAt);
        RepositoryDetailsDTO repositoryDetailsDTO = new RepositoryDetailsDTO(owner, repositoryName, "description", "cloneUrl", 5, createdAt);
        RepositoryDetails savedEntity = new RepositoryDetails(1L, owner, repositoryName, "description", "cloneUrl", 5, createdAt);

        when(gitHubDataService.getRepositoryDetails(eq(owner), eq(repositoryName))).thenReturn(gitHubData);
        when(repositoryDetailsMapper.dtoToEntity(any(RepositoryDetailsDTO.class))).thenReturn(new RepositoryDetails());
        when(repositoryDetailsRepository.save(any(RepositoryDetails.class))).thenReturn(savedEntity);
        when(repositoryDetailsMapper.entityToDto(any(RepositoryDetails.class))).thenReturn(repositoryDetailsDTO);

        RepositoryDetailsDTO result = repositoryDetailsService.saveRepositoryDetails(owner, repositoryName);

        assertNotNull(result);
        assertEquals(owner, result.getOwner());
        assertEquals(repositoryName, result.getRepositoryName());
    }

    @Test
    public void getRepositoryDetails_WhenRepositoryExists_ReturnsRepositoryDetailsDTO() {
        String owner = "owner";
        String repositoryName = "repo";
        RepositoryDetails repositoryDetails = new RepositoryDetails(1L, owner, repositoryName, "description", "cloneUrl", 5, LocalDateTime.now());

        when(repositoryDetailsRepository.findByOwnerAndRepositoryName(eq(owner), eq(repositoryName))).thenReturn(Optional.of(repositoryDetails));
        when(repositoryDetailsMapper.entityToDto(any(RepositoryDetails.class))).thenReturn(new RepositoryDetailsDTO());

        Optional<RepositoryDetailsDTO> result = repositoryDetailsService.getRepositoryDetails(owner, repositoryName);

        assertTrue(result.isPresent());
    }
}
