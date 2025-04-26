package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "`optionally: messages`")
public class OptionallyMessage {
    @Id
    @Column(name = "` chat_id`", nullable = false)
    private Integer id;

    @Column(name = "user_id", length = 45)
    private String userId;

    @Column(name = "content", length = 45)
    private String content;

    @Column(name = "timestamp", length = 45)
    private String timestamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}