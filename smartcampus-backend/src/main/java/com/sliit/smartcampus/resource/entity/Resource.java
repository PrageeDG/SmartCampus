package com.sliit.smartcampus.resource.entity;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "resources")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resource {

    @Id
    private UUID id;

    @NotBlank(message = "Name is required")
    private String name;

    private ResourceType type;

    private Integer capacity;

    @NotBlank(message = "Location is required")
    private String location;

    private ResourceStatus status;

    private List<String> amenities;

    private String imageUrl;

    private List<String> tags;

    private String aiSummary;

    @Builder.Default
    private boolean archived = false;

    private String qrCode;

    private String availabilityWindows;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
