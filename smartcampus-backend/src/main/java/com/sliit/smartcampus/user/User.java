package com.sliit.smartcampus.user;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    private UUID id;

    private String email;

    private String name;

    private String pictureUrl;

    private Role role;

    private String provider;

    @Builder.Default
    private NotifPrefs notifPrefs = new NotifPrefs();

    @CreatedDate
    private LocalDateTime createdAt;
}
