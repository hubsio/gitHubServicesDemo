package com.example.demo.model.client;

import com.example.demo.model.dto.GitHubDataDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "gitHubFeignClient", url = "${github.api.url}")
public interface GitHubFeignClient {
    @GetMapping("/repos/{owner}/{repo}")
    GitHubDataDto getRepositoryDetails(@PathVariable("owner") String owner, @PathVariable("repo") String repo);
    @GetMapping("/users/{owner}/repos")
    List<GitHubDataDto> getUserRepositories(@PathVariable("owner") String owner);
}
