package ru.netology.manager;

import ru.netology.comparator.IssueCreatedAtComparator;
import ru.netology.domain.Issue;
import ru.netology.repository.IssuesRepository;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IssuesManager {
    IssuesRepository repository;

    public IssuesManager(IssuesRepository repository) {
        this.repository = repository;
    }

    public void add(Issue issue) {
        repository.add(issue);
    }

    public Issue[] getOpenIssues() {
        Comparator<Issue> defaultComparator = new IssueCreatedAtComparator();

        return getOpenIssues(defaultComparator);
    }

    public Issue[] getOpenIssues(Comparator<Issue> comparator) {
        Collection<Issue> issues = repository.getAll();

        Issue[] openIssues = new Issue[0];

        for (Issue issue: issues) {
            if (issue.isOpen()) {
                int length = openIssues.length + 1;
                Issue[] tmp = new Issue[length];

                System.arraycopy(openIssues, 0, tmp, 0, openIssues.length);

                int lastIndex = tmp.length - 1;
                tmp[lastIndex] = issue;

                openIssues = tmp;
            }
        }

        Arrays.sort(openIssues, comparator);

        return openIssues;
    }

    public void closeIssueById(int id) {
        repository.getById(id).setOpen(false);
    }

    public void openIssueById(int id) {
        repository.getById(id).setOpen(true);
    }

    public List<Issue> filterBy(Predicate<Issue> predicate) {
        Comparator<Issue> defaultComparator = new IssueCreatedAtComparator();

        return filterBy(predicate, defaultComparator);
    }

    public List<Issue> filterBy(Predicate<Issue> predicate, Comparator<Issue> comparator) {
        return repository.getAll()
                .stream()
                .filter(predicate)
                .sorted(comparator)
                .collect(Collectors.<Issue>toList());
    }
}
