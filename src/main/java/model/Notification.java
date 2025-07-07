package model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String body;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private Long targetUserId; // null = global notification

    private LocalDateTime createdAt = LocalDateTime.now();
    private boolean read = false;

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }

    // --- Getters & Setters ---
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }

    public void setBody(String body) { this.body = body; }

    public NotificationType getType() { return type; }

    public void setType(NotificationType type) { this.type = type; }

    public Long getTargetUserId() { return targetUserId; }

    public void setTargetUserId(Long targetUserId) { this.targetUserId = targetUserId; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
