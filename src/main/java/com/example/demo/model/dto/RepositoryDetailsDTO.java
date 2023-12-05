package com.example.demo.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Builder
public class RepositoryDetailsDTO {
    private final String owner;
    private final String repositoryName;
    private final String description;
    private final String cloneUrl;
    private final Integer stars;
    private final LocalDateTime createdAt;
}
