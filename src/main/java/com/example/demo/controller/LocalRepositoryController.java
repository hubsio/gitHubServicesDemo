package com.example.demo.controller;

import com.example.demo.model.dto.RepositoryDetailsDTO;
import com.example.demo.service.GitHubDataService;
import com.example.demo.service.RepositoryDetailsService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repositories")
@RequiredArgsConstructor
@Builder
public class LocalRepositoryController {
    private final RepositoryDetailsService repositoryDetailsService;
    private final GitHubDataService gitHubDataService;

    @PostMapping("/{owner}/{repositoryName}")
    @ResponseStatus(HttpStatus.CREATED)
    public RepositoryDetailsDTO saveRepositoryDetails(@PathVariable String owner, @PathVariable String repositoryName) {
        return repositoryDetailsService.saveRepositoryDetails(owner, repositoryName);
    }

    @GetMapping("/{owner}/{repositoryName}")
    public RepositoryDetailsDTO getRepositoryDetails(@PathVariable String owner, @PathVariable String repositoryName) {
        return repositoryDetailsService.getRepositoryDetails(owner, repositoryName);
    }

    @DeleteMapping("/{owner}/{repositoryName}")
    public void deleteRepository(@PathVariable String owner, @PathVariable String repositoryName) {
        repositoryDetailsService.deleteRepository(owner, repositoryName);
    }

    @PutMapping("/{owner}/{repositoryName}")
    public RepositoryDetailsDTO updateRepositoryDetails(@PathVariable String owner, @PathVariable String repositoryName, @RequestBody RepositoryDetailsDTO updatedDetails) {
        return repositoryDetailsService.updateRepositoryDetails(owner, repositoryName, updatedDetails);
    }
}
