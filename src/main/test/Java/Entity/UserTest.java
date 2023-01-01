package Java.Entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

import ProjectManagement.entities.enums.UserSource;
import ProjectManagement.entities.User;

class UserTest {

    @Test
    void testGetId() {
        User user = new User("test@example.com", "testuser", "password", UserSource.LOCAL);
        assertEquals(0, user.getId());
    }

    @Test
    void testSetId() {
        User user = new User("test@example.com", "testuser", "password", UserSource.LOCAL);
        user.setId(1);
        assertEquals(1, user.getId());
    }

    @Test
    void testGetEmail() {
        User user = new User("test@example.com", "testuser", "password", UserSource.LOCAL);
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void testSetEmail() {
        User user = new User("test@example.com", "testuser", "password", UserSource.LOCAL);
        user.setEmail("new@example.com");
        assertEquals("new@example.com", user.getEmail());
    }

    @Test
    void testGetUsername() {
        User user = new User("test@example.com", "testuser", "password", UserSource.LOCAL);
        assertEquals("testuser", user.getUsername());
    }

    @Test
    void testSetUsername() {
        User user = new User("test@example.com", "testuser", "password", UserSource.LOCAL);
        user.setUsername("newuser");
        assertEquals("newuser", user.getUsername());
    }

    @Test
    void testGetPassword() {
        User user = new User("test@example.com", "testuser", "password", UserSource.LOCAL);
        assertEquals("password", user.getPassword());
    }

    @Test
    void testSetPassword() {
        User user = new User("test@example.com", "testuser", "password", UserSource.LOCAL);
        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword());
    }

    @Test
    void testGetSource() {
        User user = new User("test@example.com", "testuser", "password", UserSource.LOCAL);
        assertEquals(UserSource.LOCAL, user.getSource());
    }

    @Test
    void testSetSource() {
        User user = new User("test@example.com", "testuser", "password", UserSource.LOCAL);
        user.setSource(UserSource.GOOGLE);
        assertEquals(UserSource.GOOGLE, user.getSource());
    }

    @Test
    void testGetRegistrationDate() {
        User user = new User("test@example.com", "testuser", "password", UserSource.LOCAL);
        LocalDateTime now = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        assertTrue(Math.abs(now.toEpochSecond(zoneId.getRules().getOffset(now)) - user.getRegistrationDate().toEpochSecond(zoneId.getRules().getOffset(user.getRegistrationDate()))) < 1);
    }

    @Test
    void testSetRegistrationDate() {
        User user = new User("test@example.com", "testuser", "password", UserSource.LOCAL);
        LocalDateTime now = LocalDateTime.now();
        user.setRegistrationDate(now);
        assertEquals(now, user.getRegistrationDate());
    }

    @Test
    void testNoArgsConstructor() {
        User user = new User();
        assertEquals(0, user.getId());
        assertNull(user.getEmail());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertNull(user.getSource());
        assertNull(user.getRegistrationDate());
    }

    @Test
    void testConstructorWithArguments() {
        User user = new User("test@example.com", "testuser", "password", UserSource.LOCAL);
        assertEquals(0, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("testuser", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals(UserSource.LOCAL, user.getSource());
        LocalDateTime now = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        assertTrue(Math.abs(now.toEpochSecond(zoneId.getRules().getOffset(now)) - user.getRegistrationDate().toEpochSecond(zoneId.getRules().getOffset(user.getRegistrationDate()))) < 1);
    }

}
