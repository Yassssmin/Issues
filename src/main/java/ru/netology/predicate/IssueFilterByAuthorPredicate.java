package ru.netology.predicate;

import ru.netology.domain.Issue;
import ru.netology.domain.User;

import java.util.TreeSet;
import java.util.function.Predicate;

public class IssueFilterByAuthorPredicate implements Predicate<Issue> {
    private TreeSet<User> users;

    public IssueFilterByAuthorPredicate(TreeSet<User> users) {
        this.users = users;
    }

    @Override
    public boolean test(Issue issue) {
        return users.isEmpty() || users.contains(issue.getAuthor());
    }
}
