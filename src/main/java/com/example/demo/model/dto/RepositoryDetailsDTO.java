package com.example.demo.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepositoryDetailsDTO {
    private String owner;
    private String repositoryName;
    private String description;
    private String cloneUrl;
    private int stars;
    private LocalDateTime createdAt;
}
