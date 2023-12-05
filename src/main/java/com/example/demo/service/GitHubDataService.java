package com.example.demo.service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.client.GitHubFeignClient;
import com.example.demo.model.dto.GitHubDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GitHubDataService {
    private final GitHubFeignClient gitHubFeignClient;

    public GitHubDataDto getRepositoryDetails(String owner, String repo) {
        log.info("Getting details for repository: {} owned by: {}", repo, owner);
        return gitHubFeignClient.getRepositoryDetails(owner, repo);
    }

    public List<GitHubDataDto> getUserRepositories(String owner) {
        log.info("Getting repositories for user: {}", owner);
        List<GitHubDataDto> repositories = gitHubFeignClient.getUserRepositories(owner);

        if (repositories == null || repositories.isEmpty()) {
            throw new BadRequestException("Your request is not correct or no repositories found.");
        }
        return repositories;
    }
}
