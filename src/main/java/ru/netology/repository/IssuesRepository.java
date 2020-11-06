package ru.netology.repository;

import ru.netology.domain.Issue;

import java.util.ArrayList;
import java.util.Collection;

public class IssuesRepository {
    private Collection<Issue> issues = new ArrayList<>();

    public void add(Issue issue) {
        issues.add(issue);
    }

    public Collection<Issue> getAll() {
        return issues;
    }

    public Issue getById(int id) {
        for (Issue issue: issues) {
            if (issue.getId() == id) {
                return issue;
            }
        }

        return null;
    }
}
