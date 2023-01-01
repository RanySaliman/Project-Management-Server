package Java.Entity;

import ProjectManagement.entities.Comment;
import ProjectManagement.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class CommentTest {
    private static final String TEST_CONTENT = "test content";
    private static final LocalDateTime TEST_TIME = LocalDateTime.now();
    private static final User TEST_USER = new User();

    @Test
    public void testNoArgsConstructor() {
        Comment comment = new Comment();
        assertNotNull(comment);
    }

    @Test
    public void testConstructor() {
        Comment comment = new Comment(TEST_CONTENT, TEST_USER);
        assertEquals(TEST_CONTENT, comment.getContent());
        assertEquals(TEST_USER, comment.getCreator());
        assertNotNull(comment.getTime());
    }

    @Test
    public void testGettersAndSetters() {
        Comment comment = new Comment();
        comment.setContent(TEST_CONTENT);
        comment.setTime(TEST_TIME);
        comment.setCreator(TEST_USER);
        assertEquals(TEST_CONTENT, comment.getContent());
        assertEquals(TEST_TIME, comment.getTime());
        assertEquals(TEST_USER, comment.getCreator());
    }
}