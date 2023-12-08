package com.example.demo.service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.client.GitHubFeignClient;
import com.example.demo.model.dto.GitHubDataDto;
import com.example.demo.model.dto.RepositoryDetailsDTO;
import com.example.demo.model.entity.RepositoryDetails;
import com.example.demo.repository.RepositoryDetailsRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GitHubDataServiceTest {
    @Mock
    private GitHubFeignClient gitHubFeignClient;
    @Mock
    private RepositoryDetailsRepository repositoryDetailsRepository;
    @Mock
    private GitHubDataService gitHubDataService;
    @InjectMocks
    private RepositoryDetailsService repositoryDetailsService;

    @BeforeEach
    void setUp() {
        gitHubFeignClient = mock(GitHubFeignClient.class);
        this.gitHubDataService = new GitHubDataService(gitHubFeignClient);
    }

    @Test
    void getRepositoryDetails_ValidDetails_ReturnsGitHubDataDto() {
        String owner = "owner";
        String repo = "repo";
        GitHubDataDto expectedDto = GitHubDataDto.builder()
                .full_name("owner/repo")
                .description("Sample description")
                .clone_url("https://github.com/owner/repo.git")
                .stars(5)
                .created_at(LocalDateTime.now())
                .build();

        when(gitHubFeignClient.getRepositoryDetails(owner, repo)).thenReturn(expectedDto);

        GitHubDataDto actualDto = gitHubDataService.getRepositoryDetails(owner, repo);

        assertNotNull(actualDto);
        assertEquals(expectedDto, actualDto);
        verify(gitHubFeignClient, times(1)).getRepositoryDetails(owner, repo);
    }

    @Test
    void getUserRepositories_ValidOwner_ReturnsListOfGitHubDataDto() {
        String owner = "owner";
        List<GitHubDataDto> expectedRepositories = Collections.singletonList(GitHubDataDto.builder()
                .full_name("owner/repo1")
                .description("Sample description 1")
                .clone_url("https://github.com/owner/repo1.git")
                .stars(3)
                .created_at(LocalDateTime.now())
                .build());

        when(gitHubFeignClient.getUserRepositories(owner)).thenReturn(expectedRepositories);

        List<GitHubDataDto> actualRepositories = gitHubDataService.getUserRepositories(owner);

        assertNotNull(actualRepositories);
        assertEquals(expectedRepositories, actualRepositories);
        verify(gitHubFeignClient, times(1)).getUserRepositories(owner);
    }

    @Test
    void getUserRepositories_NoRepositoriesFound_ThrowsBadRequestException() {
        String owner = "hubsio";

        when(gitHubFeignClient.getUserRepositories(owner)).thenReturn(Collections.emptyList());

        assertThrows(BadRequestException.class, () -> gitHubDataService.getUserRepositories(owner));
        verify(gitHubFeignClient, times(1)).getUserRepositories(owner);
    }
}

