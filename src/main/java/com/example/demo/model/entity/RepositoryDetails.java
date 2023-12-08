package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "repositories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "owner")
    private String owner;
    @Column(name = "repository_name")
    private String repositoryName;
    @Column(name = "description")
    private String description;
    @Column(name = "clone_url")
    private String cloneUrl;
    @Column(name = "stars")
    private int stars;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
