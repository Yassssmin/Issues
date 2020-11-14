package ru.netology.predicate;

import ru.netology.domain.Issue;

import java.util.TreeSet;
import java.util.function.Predicate;

public class IssueFilterByLabelPredicate implements Predicate<Issue> {
    private TreeSet<String> labels;

    public IssueFilterByLabelPredicate(TreeSet<String> labels) {
        this.labels = labels;
    }

    @Override
    public boolean test(Issue issue) {
        return issue.getLabels().containsAll(labels);
    }
}
