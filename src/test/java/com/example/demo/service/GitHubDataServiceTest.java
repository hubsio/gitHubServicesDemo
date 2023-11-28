package com.example.demo.service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.dto.GitHubDataDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GitHubDataServiceTest {
    RestTemplate restTemplate;
    GitHubDataService gitHubDataService;

    @BeforeEach
    void setUp() {
        this.restTemplate = Mockito.mock(RestTemplate.class);
        this.gitHubDataService = new GitHubDataService(restTemplate);
    }

    @Test
    void testGetRepositoryDetails_getByUserAndRepo_success() {
        String owner = "hubsio";
        String repo = "medical-clinic";
        GitHubDataDto expectedDto = new GitHubDataDto();
        String apiUrl = "https://api.github.com/repos/" + owner + "/" + repo;
        when(restTemplate.getForObject(apiUrl, GitHubDataDto.class)).thenReturn(expectedDto);

        GitHubDataDto result = gitHubDataService.getRepositoryDetails(owner, repo);

        assertNotNull(result);
    }

    @Test
    void testGetUserRepository_getPublicRepositories_success() {
        String owner = "hubsio";
        List<GitHubDataDto> expectedList = Arrays.asList(new GitHubDataDto(), new GitHubDataDto());

        String apiUrl = "https://api.github.com/users/" + owner + "/repos";
        ResponseEntity<List<GitHubDataDto>> responseEntity = new ResponseEntity<>(expectedList, HttpStatus.OK);
        when(restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<GitHubDataDto>>() {})
        ).thenReturn(responseEntity);

        List<GitHubDataDto> result = gitHubDataService.getUserRepositories(owner);

        assertNotNull(result);
        assertEquals(expectedList.size(), result.size());
        assertEquals(expectedList.get(0).getName(), result.get(0).getName());
        assertEquals(expectedList.get(1).getDescription(), result.get(1).getDescription());
    }

    @Test
    void testGetUserRepositories_getPublicRepositories_failure() {
        String owner = "udfgsgdfsdfgsgfd";
        String apiUrl = "https://api.github.com/users/" + owner + "/repos";
        ResponseEntity<List<GitHubDataDto>> responseEntity = new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST);

        when(restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<GitHubDataDto>>() {})
        ).thenReturn(responseEntity);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> gitHubDataService.getUserRepositories(owner));
        assertEquals("Your request is not correct.", exception.getMessage());
    }

}
