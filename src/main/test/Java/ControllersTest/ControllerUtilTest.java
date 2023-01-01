package Java.ControllersTest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import ProjectManagement.controllers.ControllerUtil;
import org.junit.jupiter.api.Test;

class ControllerUtilTest {

    @Test
    void testConvertOffsetToLocalDateTime() {
        String offsetDateTimeString = "2022-12-31T12:34:56+02:00";
        Optional<LocalDateTime> expected = Optional.of(LocalDateTime.of(2022, 12, 31, 12, 34, 56));
        Optional<LocalDateTime> actual = ControllerUtil.convertOffsetToLocalDateTime(offsetDateTimeString);
        assertEquals(expected, actual);
    }

    @Test
    void testConvertOffsetToLocalDateTime_nullInput() {
        String offsetDateTimeString = null;
        Optional<LocalDateTime> expected = Optional.empty();
        Optional<LocalDateTime> actual = ControllerUtil.convertOffsetToLocalDateTime(offsetDateTimeString);
        assertEquals(expected, actual);
    }

    @Test
    void testConvertOffsetToLocalDateTime_emptyInput() {
        String offsetDateTimeString = "";
        Optional<LocalDateTime> expected = Optional.empty();
        Optional<LocalDateTime> actual = ControllerUtil.convertOffsetToLocalDateTime(offsetDateTimeString);
        assertEquals(expected, actual);
    }

    @Test
    void testConvertOffsetToLocalDateTime_invalidInput() {
        String offsetDateTimeString = "invalid input";
        Optional<LocalDateTime> expected = Optional.empty();
        Optional<LocalDateTime> actual = ControllerUtil.convertOffsetToLocalDateTime(offsetDateTimeString);
        assertEquals(expected, actual);
    }

}
