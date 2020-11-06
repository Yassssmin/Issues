package ru.netology.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.TreeSet;
import java.util.function.Predicate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Issue {
    private int id;
    private String title;
    private String description;
    private User author;
    private Collection<User> assignees = new TreeSet<>();
    private Collection<Comment> comments = new TreeSet<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isOpen;
    private Collection<String> labels = new TreeSet<>();

    public static Predicate<Issue> filterByAuthor(TreeSet<User> users) {
        return p -> users.contains(p.getAuthor());
    }

    public static Predicate<Issue> filterByLabel(TreeSet<String> labels) {
        return p -> p.getLabels().containsAll(labels);
    }

    public static Predicate<Issue> filterByAssignee(TreeSet<User> users) {
        return p -> p.getAssignees().containsAll(users);
    }
}
