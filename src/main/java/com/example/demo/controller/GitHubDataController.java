package com.example.demo.controller;

import com.example.demo.model.dto.GitHubDataDto;
import com.example.demo.service.GitHubDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/github-details")
@RequiredArgsConstructor
public class GitHubDataController {
    private final GitHubDataService gitHubDataService;

    @Operation(summary = "Get repository details", tags = "Owner(user) and repo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = GitHubDataDto.class)))})
    })
    @GetMapping("/{owner}/{repo}")
    public GitHubDataDto getRepositoryDetails(@PathVariable String owner, @PathVariable String repo) {
        return gitHubDataService.getRepositoryDetails(owner, repo);
    }

    @Operation(summary = "Get user repositories", tags = "User repositories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = GitHubDataDto.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data")))
    })
    @GetMapping("/{owner}")
    public List<GitHubDataDto> getUserRepositories(@PathVariable String owner) {
        return gitHubDataService.getUserRepositories(owner);
    }
}
