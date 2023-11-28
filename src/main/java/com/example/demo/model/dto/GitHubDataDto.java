package com.example.demo.model.dto;

import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GitHubDataDto {
    private String name;
    private String full_name;
    private String description;
    private String url;
    private int stargazersCount;
}
