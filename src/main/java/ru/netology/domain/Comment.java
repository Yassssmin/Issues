package ru.netology.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Comment implements Comparable<Comment> {
    private int id;
    private User author;
    private String text;
    private LocalDateTime createdAt;

    @Override
    public int compareTo(Comment o) {
        return this.getCreatedAt().compareTo(o.getCreatedAt());
    }
}
