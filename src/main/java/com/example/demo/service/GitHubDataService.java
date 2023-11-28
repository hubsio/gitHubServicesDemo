package com.example.demo.service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.dto.GitHubDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GitHubDataService {
    private final RestTemplate restTemplate;

    public GitHubDataDto getRepositoryDetails(String owner, String repo) {
        String apiUrl = "https://api.github.com/repos/" + owner + "/" + repo;
        log.info("Getting details for repository: {} owned by: {}", repo, owner);
        return restTemplate.getForObject(apiUrl, GitHubDataDto.class);
    }

    public List<GitHubDataDto> getUserRepositories(String owner) {
        String apiUrl = "https://api.github.com/users/" + owner + "/repos";
        log.info("Getting repositories for user: {}", owner);
        ResponseEntity<List<GitHubDataDto>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<GitHubDataDto>>() {
                }
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new BadRequestException("Your request is not correct.");
        }
    }
}
