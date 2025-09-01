package com.example.fresco.global.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class StringToShortListConverter implements Converter<String, List<Short>> {

    @Override
    public List<Short> convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String cleanSource = source.replaceAll("[\\[\\]\\s]", "");

        try {
            return Arrays.stream(cleanSource.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Short::valueOf)
                    .toList();
        } catch (NumberFormatException e) {
            return new ArrayList<>();
        }
    }
}
