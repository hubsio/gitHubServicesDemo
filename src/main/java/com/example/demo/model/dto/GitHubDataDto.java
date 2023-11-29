package com.example.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class GitHubDataDto {

    @JsonProperty("fullName")
    private final String full_name;

    private final String description;

    @JsonProperty("cloneUrl")
    private final String clone_url;

    @JsonProperty("stars")
    private final int stars;

    @JsonProperty("createdAt")
    private final LocalDateTime created_at;
}
