package ru.netology.comparators;

import ru.netology.domain.Issue;

import java.util.Comparator;

public class IssueUpdatedAtComparator implements Comparator<Issue> {
    @Override
    public int compare(Issue o1, Issue o2) {
        return o1.getUpdatedAt().compareTo(o2.getUpdatedAt());
    }
}
