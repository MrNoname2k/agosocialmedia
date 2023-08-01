package org.api.configuration;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTypeAdapter extends TypeAdapter<Date> {

    private final SimpleDateFormat dateFormat;

    public DateTypeAdapter(String dateFormat) {
        this.dateFormat = new SimpleDateFormat(dateFormat);
    }

    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            String formattedDate = dateFormat.format(value);
            out.value(formattedDate);
        }
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        if (in.hasNext()) {
            String dateStr = in.nextString();
            try {
                SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                return inputDateFormat.parse(dateStr);
            } catch (ParseException e) {
                throw new IOException("Failed to parse date: " + dateStr, e);
            }
        }
        return null;
    }
}
