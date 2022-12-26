package ProjectManagement.controllers;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ControllerUtil {
    public static Optional<LocalDateTime> convertOffsetToLocalDateTime(String offsetDateTimeString){
        if(offsetDateTimeString == null || offsetDateTimeString.isEmpty()){
            return Optional.empty();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(offsetDateTimeString, formatter);
        ZonedDateTime zoned = offsetDateTime.atZoneSameInstant(ZoneId.systemDefault());
        return Optional.of(zoned.toLocalDateTime());
    }
}
