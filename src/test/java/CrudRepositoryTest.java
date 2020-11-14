import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import ru.netology.comparator.IssueCommentsCountComparator;
import ru.netology.comparator.IssueCreatedAtComparator;
import ru.netology.comparator.IssueUpdatedAtComparator;
import ru.netology.domain.Comment;
import ru.netology.domain.Issue;
import ru.netology.domain.User;
import ru.netology.manager.IssuesManager;
import ru.netology.predicate.IssueFilterByAssigneePredicate;
import ru.netology.predicate.IssueFilterByAuthorPredicate;
import ru.netology.predicate.IssueFilterByLabelPredicate;
import ru.netology.repository.IssuesRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class CrudRepositoryTest {
    private Issue firstIssue;
    private Issue secondIssue;
    private Issue thirdIssue;
    private Issue fourthIssue;

    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private User user5;

    private String label1;
    private String label2;
    private String label3;
    private String label4;

    @BeforeEach
    void setUp() {
        user1 = new User(1, "User 1", "test");
        user2 = new User(2, "User 2", "test");
        user3 = new User(3, "User 3", "test");
        user4 = new User(4, "User 4", "test");
        user5 = new User(5, "User 5", "test");

        Comment comment1 = new Comment(1, user2, "test", LocalDateTime.now());
        Comment comment2 = new Comment(2, user2, "test", LocalDateTime.now());

        label1 = "Label 1";
        label2 = "Label 2";
        label3 = "Label 3";
        label4 = "Label 4";

        firstIssue = new Issue();
        firstIssue.setId(1);
        firstIssue.setTitle("Test");
        firstIssue.setDescription("There were text");
        firstIssue.setAuthor(user1);
        firstIssue.getAssignees().add(user1);
        firstIssue.getComments().add(comment1);
        firstIssue.setCreatedAt(LocalDateTime.of(2019, 3, 7, 10, 25));
        firstIssue.setUpdatedAt(LocalDateTime.of(2019, 7, 2, 14, 30));
        firstIssue.setOpen(true);
        firstIssue.getLabels().add(label1);
        firstIssue.getLabels().add(label2);

        secondIssue = new Issue();
        secondIssue.setId(2);
        secondIssue.setTitle("Test");
        secondIssue.setDescription("There were text");
        secondIssue.setAuthor(user3);
        secondIssue.getAssignees().add(user1);
        secondIssue.getAssignees().add(user3);
        secondIssue.getComments().add(comment1);
        secondIssue.getComments().add(comment2);
        secondIssue.setCreatedAt(LocalDateTime.of(2020, 1, 15, 16, 50));
        secondIssue.setUpdatedAt(LocalDateTime.of(2020, 3, 11, 16, 50));
        secondIssue.setOpen(false);
        secondIssue.getLabels().add(label1);

        thirdIssue = new Issue();
        thirdIssue.setId(3);
        thirdIssue.setTitle("Test");
        thirdIssue.setDescription("There were text");
        thirdIssue.setAuthor(user4);
        thirdIssue.setCreatedAt(LocalDateTime.of(2018, 12, 3, 18, 35));
        thirdIssue.setUpdatedAt(LocalDateTime.of(2019, 2, 25, 17, 40));
        thirdIssue.setOpen(true);

        fourthIssue = new Issue();
        fourthIssue.setId(4);
        fourthIssue.setTitle("Test");
        fourthIssue.setDescription("There were text");
        fourthIssue.setAuthor(user4);
        fourthIssue.setCreatedAt(LocalDateTime.of(2017, 11, 20, 8,12));
        fourthIssue.setUpdatedAt(LocalDateTime.of(2018, 8, 16, 20, 30));
        fourthIssue.setOpen(false);
    }

    @Nested
    class Empty {
        private IssuesManager manager;
        private IssuesRepository repository;

        @BeforeEach
        void setUp() {
            repository = new IssuesRepository();
            manager = new IssuesManager(repository);
        }

        @Test
        void shouldAddIssueToRepository() {
            manager.add(firstIssue);

            Collection<Issue> expected = new ArrayList<>();
            expected.add(firstIssue);

            Collection<Issue> actual = repository.getAll();

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldNotGetOpenIssues() {
            Issue[] expected = new Issue[0];
            Issue[] actual = manager.getOpenIssues();

            assertArrayEquals(expected, actual);
        }

        @Test
        void shouldNotGetOpenIssuesWithCommentsCountComparator() {
            Comparator<Issue> comparator = new IssueCommentsCountComparator();

            Issue[] expected = new Issue[0];
            Issue[] actual = manager.getOpenIssues(comparator);

            assertArrayEquals(expected, actual);
        }

        @Test
        void shouldNotGetOpenIssuesWithCreatedAtComparator() {
            Comparator<Issue> comparator = new IssueCreatedAtComparator();

            Issue[] expected = new Issue[0];
            Issue[] actual = manager.getOpenIssues(comparator);

            assertArrayEquals(expected, actual);
        }

        @Test
        void shouldNotGetOpenIssuesWithUpdatedAtComparator() {
            Comparator<Issue> comparator = new IssueUpdatedAtComparator();

            Issue[] expected = new Issue[0];
            Issue[] actual = manager.getOpenIssues(comparator);

            assertArrayEquals(expected, actual);
        }

        @Test
        void shouldNotCloseIssueById() {
            assertThrows(NullPointerException.class, () -> manager.closeIssueById(999));
        }

        @Test
        void shouldNotOpenIssueById() {
            assertThrows(NullPointerException.class, () -> manager.openIssueById(999));
        }

        @Test
        void shouldNotFilterIssuesWithAssigneePredicate() {
            TreeSet<User> assigneeSet = new TreeSet<>();
            assigneeSet.add(user1);
            Predicate<Issue> predicate = new IssueFilterByAssigneePredicate(assigneeSet);

            List<Issue> expected = new ArrayList<>();
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldNotFilterIssuesWithAssigneePredicateEmptyAssigneeSet() {
            TreeSet<User> assigneeSet = new TreeSet<>();
            Predicate<Issue> predicate = new IssueFilterByAssigneePredicate(assigneeSet);

            List<Issue> expected = new ArrayList<>();
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldNotFilterIssuesWithAuthorPredicate() {
            TreeSet<User> authorSet = new TreeSet<>();
            authorSet.add(user1);
            Predicate<Issue> predicate = new IssueFilterByAuthorPredicate(authorSet);

            List<Issue> expected = new ArrayList<>();
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldNotFilterIssuesWithAuthorPredicateEmptyAuthorSet() {
            TreeSet<User> authorSet = new TreeSet<>();
            Predicate<Issue> predicate = new IssueFilterByAuthorPredicate(authorSet);

            List<Issue> expected = new ArrayList<>();
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldNotFilterIssuesWithLabelPredicate() {
            TreeSet<String> labelSet = new TreeSet<>();
            labelSet.add(label1);
            Predicate<Issue> predicate = new IssueFilterByLabelPredicate(labelSet);

            List<Issue> expected = new ArrayList<>();
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldNotFilterIssuesWithLabelPredicateEmptyLabelSet() {
            TreeSet<String> labelSet = new TreeSet<>();
            Predicate<Issue> predicate = new IssueFilterByLabelPredicate(labelSet);

            List<Issue> expected = new ArrayList<>();
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldNotFilterIssuesWithCommentCountComparator() {
            TreeSet<String> labelSet = new TreeSet<>();
            Predicate<Issue> predicate = new IssueFilterByLabelPredicate(labelSet);

            Comparator<Issue> comparator = new IssueCommentsCountComparator();

            List<Issue> expected = new ArrayList<>();
            List<Issue> actual = manager.filterBy(predicate, comparator);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldNotFilterIssuesWithCreatedAtComparator() {
            TreeSet<String> labelSet = new TreeSet<>();
            Predicate<Issue> predicate = new IssueFilterByLabelPredicate(labelSet);

            Comparator<Issue> comparator = new IssueCreatedAtComparator();

            List<Issue> expected = new ArrayList<>();
            List<Issue> actual = manager.filterBy(predicate, comparator);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldNotFilterIssuesWithUpdatedAtComparator() {
            TreeSet<String> labelSet = new TreeSet<>();
            Predicate<Issue> predicate = new IssueFilterByLabelPredicate(labelSet);

            Comparator<Issue> comparator = new IssueUpdatedAtComparator();

            List<Issue> expected = new ArrayList<>();
            List<Issue> actual = manager.filterBy(predicate, comparator);

            assertIterableEquals(expected, actual);
        }
    }

    @Nested
    class SingleItem {
        private IssuesManager manager;
        private IssuesRepository repository;

        @BeforeEach
        void setUp() {
            repository = new IssuesRepository();
            manager = new IssuesManager(repository);

            repository.add(firstIssue);
        }

        @Test
        void shouldAddIssueToRepository() {
            manager.add(secondIssue);

            Collection<Issue> expected = new ArrayList<>();
            expected.add(firstIssue);
            expected.add(secondIssue);

            Collection<Issue> actual = repository.getAll();

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldGetOpenIssues() {
            Issue[] expected = new Issue[]{firstIssue};
            Issue[] actual = manager.getOpenIssues();

            assertArrayEquals(expected, actual);
        }

        @Test
        void shouldGetOpenIssuesWithCommentsCountComparator() {
            Comparator<Issue> comparator = new IssueCommentsCountComparator();

            Issue[] expected = new Issue[]{firstIssue};
            Issue[] actual = manager.getOpenIssues(comparator);

            assertArrayEquals(expected, actual);
        }

        @Test
        void shouldGetOpenIssuesWithCreatedAtComparator() {
            Comparator<Issue> comparator = new IssueCreatedAtComparator();

            Issue[] expected = new Issue[]{firstIssue};
            Issue[] actual = manager.getOpenIssues(comparator);

            assertArrayEquals(expected, actual);
        }

        @Test
        void shouldGetOpenIssuesWithUpdatedAtComparator() {
            Comparator<Issue> comparator = new IssueUpdatedAtComparator();

            Issue[] expected = new Issue[]{firstIssue};
            Issue[] actual = manager.getOpenIssues(comparator);

            assertArrayEquals(expected, actual);
        }

        @Test
        void shouldCloseIssueById() {
            firstIssue.setOpen(true);

            manager.closeIssueById(firstIssue.getId());

            assertFalse(firstIssue.isOpen());
        }

        @Test
        void shouldNotCloseIssueByInvalidId() {
            assertThrows(NullPointerException.class, () -> manager.closeIssueById(999));
        }

        @Test
        void shouldOpenIssueById() {
            firstIssue.setOpen(false);

            manager.openIssueById(firstIssue.getId());

            assertTrue(firstIssue.isOpen());
        }

        @Test
        void shouldNotOpenIssueById() {
            assertThrows(NullPointerException.class, () -> manager.openIssueById(999));
        }

        @Test
        void shouldFilterIssuesWithAssigneePredicate() {
            TreeSet<User> assigneeSet = new TreeSet<>();
            assigneeSet.add(user1);
            Predicate<Issue> predicate = new IssueFilterByAssigneePredicate(assigneeSet);

            List<Issue> expected = new ArrayList<>();
            expected.add(firstIssue);
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldFilterIssuesWithAssigneePredicateEmptyAssigneeSet() {
            TreeSet<User> assigneeSet = new TreeSet<>();
            Predicate<Issue> predicate = new IssueFilterByAssigneePredicate(assigneeSet);

            List<Issue> expected = new ArrayList<>();
            expected.add(firstIssue);
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldNotFilterIssuesWithAssigneePredicateInvalidAssigneeSet() {
            TreeSet<User> assigneeSet = new TreeSet<>();
            assigneeSet.add(user2);
            Predicate<Issue> predicate = new IssueFilterByAssigneePredicate(assigneeSet);

            List<Issue> expected = new ArrayList<>();
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldNotFilterIssuesWithAssigneePredicateInvalidAssigneeSetMultiple() {
            TreeSet<User> assigneeSet = new TreeSet<>();
            assigneeSet.add(user1);
            assigneeSet.add(user2);
            Predicate<Issue> predicate = new IssueFilterByAssigneePredicate(assigneeSet);

            List<Issue> expected = new ArrayList<>();
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldFilterIssuesWithAuthorPredicate() {
            TreeSet<User> authorSet = new TreeSet<>();
            authorSet.add(user1);
            Predicate<Issue> predicate = new IssueFilterByAuthorPredicate(authorSet);

            List<Issue> expected = new ArrayList<>();
            expected.add(firstIssue);
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldFilterIssuesWithAuthorPredicateEmptyAuthorSet() {
            TreeSet<User> authorSet = new TreeSet<>();
            Predicate<Issue> predicate = new IssueFilterByAuthorPredicate(authorSet);

            List<Issue> expected = new ArrayList<>();
            expected.add(firstIssue);
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldFilterIssuesWithAuthorPredicateMultipleAuthorSet() {
            TreeSet<User> authorSet = new TreeSet<>();
            authorSet.add(user1);
            authorSet.add(user2);
            Predicate<Issue> predicate = new IssueFilterByAuthorPredicate(authorSet);

            List<Issue> expected = new ArrayList<>();
            expected.add(firstIssue);
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldNotFilterIssuesWithAuthorPredicateInvalidAuthorSet() {
            TreeSet<User> authorSet = new TreeSet<>();
            authorSet.add(user2);
            Predicate<Issue> predicate = new IssueFilterByAuthorPredicate(authorSet);

            List<Issue> expected = new ArrayList<>();
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldFilterIssuesWithLabelPredicate() {
            TreeSet<String> labelSet = new TreeSet<>();
            labelSet.add(label1);
            Predicate<Issue> predicate = new IssueFilterByLabelPredicate(labelSet);

            List<Issue> expected = new ArrayList<>();
            expected.add(firstIssue);
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldFilterIssuesWithLabelPredicateEmptyLabelSet() {
            TreeSet<String> labelSet = new TreeSet<>();
            Predicate<Issue> predicate = new IssueFilterByLabelPredicate(labelSet);

            List<Issue> expected = new ArrayList<>();
            expected.add(firstIssue);
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldNotFilterIssuesWithLabelPredicateInvalidLabelSet() {
            TreeSet<String> labelSet = new TreeSet<>();
            labelSet.add(label3);
            Predicate<Issue> predicate = new IssueFilterByLabelPredicate(labelSet);

            List<Issue> expected = new ArrayList<>();
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldNotFilterIssuesWithLabelPredicateInvalidLabelSetMultiple() {
            TreeSet<String> labelSet = new TreeSet<>();
            labelSet.add(label1);
            labelSet.add(label2);
            labelSet.add(label3);
            Predicate<Issue> predicate = new IssueFilterByLabelPredicate(labelSet);

            List<Issue> expected = new ArrayList<>();
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldFilterIssuesWithCommentCountComparator() {
            TreeSet<String> labelSet = new TreeSet<>();
            Predicate<Issue> predicate = new IssueFilterByLabelPredicate(labelSet);

            Comparator<Issue> comparator = new IssueCommentsCountComparator();

            List<Issue> expected = new ArrayList<>();
            expected.add(firstIssue);
            List<Issue> actual = manager.filterBy(predicate, comparator);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldFilterIssuesWithCreatedAtComparator() {
            TreeSet<String> labelSet = new TreeSet<>();
            Predicate<Issue> predicate = new IssueFilterByLabelPredicate(labelSet);

            Comparator<Issue> comparator = new IssueCreatedAtComparator();

            List<Issue> expected = new ArrayList<>();
            expected.add(firstIssue);
            List<Issue> actual = manager.filterBy(predicate, comparator);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldFilterIssuesWithUpdatedAtComparator() {
            TreeSet<String> labelSet = new TreeSet<>();
            Predicate<Issue> predicate = new IssueFilterByLabelPredicate(labelSet);

            Comparator<Issue> comparator = new IssueUpdatedAtComparator();

            List<Issue> expected = new ArrayList<>();
            expected.add(firstIssue);
            List<Issue> actual = manager.filterBy(predicate, comparator);

            assertIterableEquals(expected, actual);
        }
    }

    @Nested
    class MultipleItems {
        private IssuesManager manager;
        private IssuesRepository repository;

        @BeforeEach
        void setUp() {
            repository = new IssuesRepository();
            manager = new IssuesManager(repository);

            repository.add(firstIssue);
            repository.add(secondIssue);
            repository.add(thirdIssue);
        }

        @Test
        void shouldAddIssueToRepository() {
            manager.add(fourthIssue);

            Collection<Issue> expected = new ArrayList<>();
            expected.add(firstIssue);
            expected.add(secondIssue);
            expected.add(thirdIssue);
            expected.add(fourthIssue);

            Collection<Issue> actual = repository.getAll();

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldGetOpenIssues() {
            Issue[] expected = new Issue[]{thirdIssue, firstIssue};
            Issue[] actual = manager.getOpenIssues();

            assertArrayEquals(expected, actual);
        }

        @Test
        void shouldGetOpenIssuesWithCommentsCountComparator() {
            Comparator<Issue> comparator = new IssueCommentsCountComparator();

            Issue[] expected = new Issue[]{thirdIssue, firstIssue};
            Issue[] actual = manager.getOpenIssues(comparator);

            assertArrayEquals(expected, actual);
        }

        @Test
        void shouldGetOpenIssuesWithCommentsCountComparatorReversed() {
            Comparator<Issue> comparator = new IssueCommentsCountComparator();

            Issue[] expected = new Issue[]{firstIssue, thirdIssue};
            Issue[] actual = manager.getOpenIssues(comparator.reversed());

            assertArrayEquals(expected, actual);
        }

        @Test
        void shouldGetOpenIssuesWithCreatedAtComparator() {
            Comparator<Issue> comparator = new IssueCreatedAtComparator();

            Issue[] expected = new Issue[]{thirdIssue, firstIssue};
            Issue[] actual = manager.getOpenIssues(comparator);

            assertArrayEquals(expected, actual);
        }

        @Test
        void shouldGetOpenIssuesWithCreatedAtComparatorReversed() {
            Comparator<Issue> comparator = new IssueCreatedAtComparator();

            Issue[] expected = new Issue[]{firstIssue, thirdIssue};
            Issue[] actual = manager.getOpenIssues(comparator.reversed());

            assertArrayEquals(expected, actual);
        }

        @Test
        void shouldGetOpenIssuesWithUpdatedAtComparator() {
            Comparator<Issue> comparator = new IssueUpdatedAtComparator();

            Issue[] expected = new Issue[]{thirdIssue, firstIssue};
            Issue[] actual = manager.getOpenIssues(comparator);

            assertArrayEquals(expected, actual);
        }

        @Test
        void shouldGetOpenIssuesWithUpdatedAtComparatorReversed() {
            Comparator<Issue> comparator = new IssueUpdatedAtComparator();

            Issue[] expected = new Issue[]{firstIssue, thirdIssue};
            Issue[] actual = manager.getOpenIssues(comparator.reversed());

            assertArrayEquals(expected, actual);
        }

        @Test
        void shouldCloseIssueById() {
            firstIssue.setOpen(true);

            manager.closeIssueById(firstIssue.getId());

            assertFalse(firstIssue.isOpen());
        }

        @Test
        void shouldNotCloseIssueByInvalidId() {
            assertThrows(NullPointerException.class, () -> manager.closeIssueById(999));
        }

        @Test
        void shouldOpenIssueById() {
            firstIssue.setOpen(false);

            manager.openIssueById(firstIssue.getId());

            assertTrue(firstIssue.isOpen());
        }

        @Test
        void shouldNotOpenIssueById() {
            assertThrows(NullPointerException.class, () -> manager.openIssueById(999));
        }

        @Test
        void shouldFilterIssuesWithAssigneePredicate() {
            TreeSet<User> assigneeSet = new TreeSet<>();
            assigneeSet.add(user1);
            Predicate<Issue> predicate = new IssueFilterByAssigneePredicate(assigneeSet);

            List<Issue> expected = new ArrayList<>();
            expected.add(firstIssue);
            expected.add(secondIssue);
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldFilterIssuesWithAssigneePredicateEmptyAssigneeSet() {
            TreeSet<User> assigneeSet = new TreeSet<>();
            Predicate<Issue> predicate = new IssueFilterByAssigneePredicate(assigneeSet);

            List<Issue> expected = new ArrayList<>();
            expected.add(thirdIssue);
            expected.add(firstIssue);
            expected.add(secondIssue);

            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldNotFilterIssuesWithAssigneePredicateInvalidAssigneeSet() {
            TreeSet<User> assigneeSet = new TreeSet<>();
            assigneeSet.add(user5);
            Predicate<Issue> predicate = new IssueFilterByAssigneePredicate(assigneeSet);

            List<Issue> expected = new ArrayList<>();
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldNotFilterIssuesWithAssigneePredicateInvalidAssigneeSetMultiple() {
            TreeSet<User> assigneeSet = new TreeSet<>();
            assigneeSet.add(user1);
            assigneeSet.add(user5);
            Predicate<Issue> predicate = new IssueFilterByAssigneePredicate(assigneeSet);

            List<Issue> expected = new ArrayList<>();
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldFilterIssuesWithAuthorPredicate() {
            TreeSet<User> authorSet = new TreeSet<>();
            authorSet.add(user1);
            Predicate<Issue> predicate = new IssueFilterByAuthorPredicate(authorSet);

            List<Issue> expected = new ArrayList<>();
            expected.add(firstIssue);
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldFilterIssuesWithAuthorPredicateEmptyAuthorSet() {
            TreeSet<User> authorSet = new TreeSet<>();
            Predicate<Issue> predicate = new IssueFilterByAuthorPredicate(authorSet);

            List<Issue> expected = new ArrayList<>();
            expected.add(thirdIssue);
            expected.add(firstIssue);
            expected.add(secondIssue);
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldFilterIssuesWithAuthorPredicateMultipleAuthorSet() {
            TreeSet<User> authorSet = new TreeSet<>();
            authorSet.add(user1);
            authorSet.add(user3);
            Predicate<Issue> predicate = new IssueFilterByAuthorPredicate(authorSet);

            List<Issue> expected = new ArrayList<>();
            expected.add(firstIssue);
            expected.add(secondIssue);
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldNotFilterIssuesWithAuthorPredicateInvalidAuthorSet() {
            TreeSet<User> authorSet = new TreeSet<>();
            authorSet.add(user2);
            Predicate<Issue> predicate = new IssueFilterByAuthorPredicate(authorSet);

            List<Issue> expected = new ArrayList<>();
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldFilterIssuesWithLabelPredicate() {
            TreeSet<String> labelSet = new TreeSet<>();
            labelSet.add(label1);
            Predicate<Issue> predicate = new IssueFilterByLabelPredicate(labelSet);

            List<Issue> expected = new ArrayList<>();
            expected.add(firstIssue);
            expected.add(secondIssue);
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldFilterIssuesWithLabelPredicateEmptyLabelSet() {
            TreeSet<String> labelSet = new TreeSet<>();
            Predicate<Issue> predicate = new IssueFilterByLabelPredicate(labelSet);

            List<Issue> expected = new ArrayList<>();
            expected.add(thirdIssue);
            expected.add(firstIssue);
            expected.add(secondIssue);
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldFilterIssuesWithLabelPredicateLabelSetMultiple() {
            TreeSet<String> labelSet = new TreeSet<>();
            labelSet.add(label1);
            labelSet.add(label2);
            Predicate<Issue> predicate = new IssueFilterByLabelPredicate(labelSet);

            List<Issue> expected = new ArrayList<>();
            expected.add(firstIssue);
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldNotFilterIssuesWithLabelPredicateInvalidLabelSet() {
            TreeSet<String> labelSet = new TreeSet<>();
            labelSet.add(label3);
            Predicate<Issue> predicate = new IssueFilterByLabelPredicate(labelSet);

            List<Issue> expected = new ArrayList<>();
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldNotFilterIssuesWithLabelPredicateInvalidLabelSetMultiple() {
            TreeSet<String> labelSet = new TreeSet<>();
            labelSet.add(label1);
            labelSet.add(label2);
            labelSet.add(label3);
            Predicate<Issue> predicate = new IssueFilterByLabelPredicate(labelSet);

            List<Issue> expected = new ArrayList<>();
            List<Issue> actual = manager.filterBy(predicate);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldFilterIssuesWithCommentCountComparator() {
            TreeSet<String> labelSet = new TreeSet<>();
            Predicate<Issue> predicate = new IssueFilterByLabelPredicate(labelSet);

            Comparator<Issue> comparator = new IssueCommentsCountComparator();

            List<Issue> expected = new ArrayList<>();
            expected.add(thirdIssue);
            expected.add(firstIssue);
            expected.add(secondIssue);
            List<Issue> actual = manager.filterBy(predicate, comparator);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldFilterIssuesWithCommentCountComparatorReversed() {
            TreeSet<String> labelSet = new TreeSet<>();
            Predicate<Issue> predicate = new IssueFilterByLabelPredicate(labelSet);

            Comparator<Issue> comparator = new IssueCommentsCountComparator();

            List<Issue> expected = new ArrayList<>();
            expected.add(secondIssue);
            expected.add(firstIssue);
            expected.add(thirdIssue);

            List<Issue> actual = manager.filterBy(predicate, comparator.reversed());

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldFilterIssuesWithCreatedAtComparator() {
            TreeSet<String> labelSet = new TreeSet<>();
            Predicate<Issue> predicate = new IssueFilterByLabelPredicate(labelSet);

            Comparator<Issue> comparator = new IssueCreatedAtComparator();

            List<Issue> expected = new ArrayList<>();
            expected.add(thirdIssue);
            expected.add(firstIssue);
            expected.add(secondIssue);

            List<Issue> actual = manager.filterBy(predicate, comparator);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldFilterIssuesWithCreatedAtComparatorReversed() {
            TreeSet<String> labelSet = new TreeSet<>();
            Predicate<Issue> predicate = new IssueFilterByLabelPredicate(labelSet);

            Comparator<Issue> comparator = new IssueCreatedAtComparator();

            List<Issue> expected = new ArrayList<>();
            expected.add(secondIssue);
            expected.add(firstIssue);
            expected.add(thirdIssue);

            List<Issue> actual = manager.filterBy(predicate, comparator.reversed());

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldFilterIssuesWithUpdatedAtComparator() {
            TreeSet<String> labelSet = new TreeSet<>();
            Predicate<Issue> predicate = new IssueFilterByLabelPredicate(labelSet);

            Comparator<Issue> comparator = new IssueUpdatedAtComparator();

            List<Issue> expected = new ArrayList<>();
            expected.add(thirdIssue);
            expected.add(firstIssue);
            expected.add(secondIssue);

            List<Issue> actual = manager.filterBy(predicate, comparator);

            assertIterableEquals(expected, actual);
        }

        @Test
        void shouldFilterIssuesWithUpdatedAtComparatorReversed() {
            TreeSet<String> labelSet = new TreeSet<>();
            Predicate<Issue> predicate = new IssueFilterByLabelPredicate(labelSet);

            Comparator<Issue> comparator = new IssueUpdatedAtComparator();

            List<Issue> expected = new ArrayList<>();
            expected.add(secondIssue);
            expected.add(firstIssue);
            expected.add(thirdIssue);

            List<Issue> actual = manager.filterBy(predicate, comparator.reversed());

            assertIterableEquals(expected, actual);
        }
    }
}
