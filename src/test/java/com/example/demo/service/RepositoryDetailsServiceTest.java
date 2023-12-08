package com.example.demo.service;

import com.example.demo.exception.BadRequestException;
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
import static org.mockito.Mockito.*;

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

        when(gitHubDataService.getRepositoryDetails(owner, repositoryName)).thenReturn(gitHubData);
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

        when(repositoryDetailsRepository.findByOwnerAndRepositoryName(owner, repositoryName)).thenReturn(Optional.of(repositoryDetails));
        when(repositoryDetailsMapper.entityToDto(any(RepositoryDetails.class))).thenReturn(RepositoryDetailsDTO.builder().build());

        Optional<RepositoryDetailsDTO> result = repositoryDetailsService.getRepositoryDetails(owner, repositoryName);

        assertTrue(result.isPresent());
    }

    @Test
    void deleteRepository_Success() {
        String owner = "owner";
        String repositoryName = "repo";
        RepositoryDetails repositoryDetails = new RepositoryDetails(1L, owner, repositoryName, "description", "cloneUrl", 5, LocalDateTime.now());

        when(repositoryDetailsRepository.findByOwnerAndRepositoryName(owner, repositoryName)).thenReturn(Optional.of(repositoryDetails));

        repositoryDetailsService.deleteRepository(owner, repositoryName);

        verify(repositoryDetailsRepository, times(1)).findByOwnerAndRepositoryName(eq(owner), eq(repositoryName));
        verify(repositoryDetailsRepository, times(1)).delete(any(RepositoryDetails.class));
    }

    @Test
    void updateRepositoryDetails_Success() {
        String owner = "owner";
        String repositoryName = "repo";
        LocalDateTime createdAt = LocalDateTime.now();

        RepositoryDetailsDTO updatedDetails = new RepositoryDetailsDTO(owner, repositoryName, "description", "cloneUrl", 5, createdAt);

        RepositoryDetails existingRepository = new RepositoryDetails(1L, owner, repositoryName, "old description", "old cloneUrl", 3, LocalDateTime.now());

        when(repositoryDetailsRepository.findByOwnerAndRepositoryName(owner, repositoryName)).thenReturn(Optional.of(existingRepository));
        when(repositoryDetailsRepository.save(any(RepositoryDetails.class))).thenReturn(existingRepository);
        when(repositoryDetailsMapper.entityToDto(any(RepositoryDetails.class))).thenReturn(updatedDetails);

        RepositoryDetailsDTO result = repositoryDetailsService.updateRepositoryDetails(owner, repositoryName, updatedDetails);

        assertNotNull(result);
        assertEquals(owner, result.getOwner());
        assertEquals(repositoryName, result.getRepositoryName());
        assertEquals("description", result.getDescription());
        assertEquals("cloneUrl", result.getCloneUrl());
        assertEquals(5, result.getStars());
        assertEquals(createdAt, result.getCreatedAt());

        verify(repositoryDetailsRepository, times(1)).findByOwnerAndRepositoryName(eq(owner), eq(repositoryName));
        verify(repositoryDetailsRepository, times(1)).save(any(RepositoryDetails.class));
        verify(repositoryDetailsMapper, times(1)).entityToDto(any(RepositoryDetails.class));
    }

    @Test
    void saveRepositoryDetails_WhenRepositoryAlreadyExists_ThrowsBadRequestException() {
        String owner = "testOwner";
        String repositoryName = "testRepo";

        when(repositoryDetailsRepository.findByOwnerAndRepositoryName(owner, repositoryName)).thenReturn(Optional.of(new RepositoryDetails()));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            repositoryDetailsService.saveRepositoryDetails(owner, repositoryName);
        });

        assertEquals("Repository with name " + repositoryName + " already exists for owner " + owner, exception.getMessage());
    }

    @Test
    void deleteRepository_WhenRepositoryNotFound_ThrowsBadRequestException() {
        String owner = "hubsio";
        String repositoryName = "testRepo";

        when(repositoryDetailsRepository.findByOwnerAndRepositoryName(owner, repositoryName)).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            repositoryDetailsService.deleteRepository(owner, repositoryName);
        });

        assertEquals("Repository not found for owner: " + owner + " and repository name: " + repositoryName, exception.getMessage());
    }

    @Test
    void updateRepositoryDetails_WhenRepositoryNotFound_ThrowsBadRequestException() {
        String owner = "hubsio";
        String repositoryName = "medical-clinic";

        when(repositoryDetailsRepository.findByOwnerAndRepositoryName(owner, repositoryName)).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            RepositoryDetailsDTO repositoryDetailsDTO = RepositoryDetailsDTO.builder()
                    .owner(owner)
                    .repositoryName(repositoryName)
                    .description("Sample description")
                    .cloneUrl("https://github.com/sample/medical-clinic")
                    .stars(10)
                    .createdAt(LocalDateTime.now())
                    .build();

            repositoryDetailsService.updateRepositoryDetails(owner, repositoryName, repositoryDetailsDTO);
        });

        assertEquals("Repository not found for owner: " + owner + " and repository name: " + repositoryName, exception.getMessage());
    }
}
