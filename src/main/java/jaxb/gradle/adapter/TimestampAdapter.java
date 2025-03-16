package jaxb.gradle.adapter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class TimestampAdapter extends XmlAdapter<String, Timestamp> {
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public Timestamp unmarshal(String v) throws Exception {
        return Timestamp.valueOf(v);
    }

    public String marshal(Timestamp v) throws Exception {
        return format.format(v.toInstant());
    }
}
