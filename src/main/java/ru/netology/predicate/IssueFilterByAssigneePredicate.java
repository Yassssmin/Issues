package ru.netology.predicate;

import ru.netology.domain.Issue;
import ru.netology.domain.User;

import java.util.TreeSet;
import java.util.function.Predicate;

public class IssueFilterByAssigneePredicate implements Predicate<Issue> {
    private TreeSet<User> users;

    public IssueFilterByAssigneePredicate(TreeSet<User> users) {
        this.users = users;
    }

    @Override
    public boolean test(Issue issue) {
        return issue.getAssignees().containsAll(users);
    }
}
