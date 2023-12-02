package com.example.demo.controller;

import com.example.demo.mapper.RepositoryDetailsMapper;
import com.example.demo.model.dto.RepositoryDetailsDTO;
import com.example.demo.model.entity.RepositoryDetails;
import com.example.demo.service.GitHubDataService;
import com.example.demo.service.RepositoryDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/repositories")
@RequiredArgsConstructor
@Builder
public class RepositoryDetailsController {
    private final RepositoryDetailsService repositoryDetailsService;
    private final GitHubDataService gitHubDataService;

    @Operation(summary = "Save Repository details", tags = "User repositories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = RepositoryDetailsDTO.class)))}),
            @ApiResponse(responseCode = "400", description = "Repository already exists", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data")))
    })
    @PostMapping("/{owner}/{repositoryName}")
    @ResponseStatus(HttpStatus.CREATED)
    public RepositoryDetailsDTO saveRepositoryDetails(@PathVariable String owner, @PathVariable String repositoryName) {
        return repositoryDetailsService.saveRepositoryDetails(owner, repositoryName);
    }

    @Operation(summary = "Get repository details", tags = "User repositories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = RepositoryDetailsDTO.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data")))
    })
    @GetMapping("/{owner}/{repositoryName}")
    public Optional<RepositoryDetailsDTO> getRepositoryDetails(@PathVariable String owner, @PathVariable String repositoryName) {
        return repositoryDetailsService.getRepositoryDetails(owner, repositoryName);
    }

    @Operation(summary = "Delete repository", tags = "User repositories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = RepositoryDetailsDTO.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data")))
    })
    @DeleteMapping("/{owner}/{repositoryName}")
    public void deleteRepository(@PathVariable String owner, @PathVariable String repositoryName) {
        repositoryDetailsService.deleteRepository(owner, repositoryName);
    }

    @Operation(summary = "Update Repository Details", tags = "User repositories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = RepositoryDetailsDTO.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data")))
    })
    @PutMapping("/{owner}/{repositoryName}")
    public RepositoryDetailsDTO updateRepositoryDetails(@PathVariable String owner, @PathVariable String repositoryName, @RequestBody RepositoryDetailsDTO updatedDetails) {
        return repositoryDetailsService.updateRepositoryDetails(owner, repositoryName, updatedDetails);
    }
}
