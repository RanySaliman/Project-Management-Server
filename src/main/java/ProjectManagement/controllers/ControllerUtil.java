package ProjectManagement.controllers;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ControllerUtil {
    /**
     * converts a string contains offset datetime to a LocalDateTime object
     *
     * @param offsetDateTimeString
     * @return
     */
    public static Optional<LocalDateTime> convertOffsetToLocalDateTime(String offsetDateTimeString) {
        try {
            if (offsetDateTimeString == null || offsetDateTimeString.isEmpty()) {
                return Optional.empty();
            }
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(offsetDateTimeString, formatter);
            ZonedDateTime zoned = offsetDateTime.atZoneSameInstant(ZoneId.systemDefault());
            return Optional.of(zoned.toLocalDateTime());
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }
}
