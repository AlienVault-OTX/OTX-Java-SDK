package com.alienvault.otx.model.pulse;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Date;

public class OtxDateDeserializer extends JsonDeserializer<Date> {
    private static final DateTimeFormatter FORMATTER =
            new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm:ss") // .parseLenient()
                    .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
                    .toFormatter();
    @Override
    public Date deserialize(JsonParser jsonparser,
                            DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {

        String date = jsonparser.getText();
        LocalDateTime localDate = LocalDateTime.parse(date, FORMATTER);
        Date convertedDatetime = Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
        try {
            return convertedDatetime;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
