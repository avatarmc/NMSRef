package com.avatarmc.ref.maven;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.LineReader;

final class ListReader {
    public static Set<String> read(InputStream is) throws IOException {
        ImmutableSet.Builder<String> out = ImmutableSet.builder();

        try (Reader reader = new InputStreamReader(
                is, StandardCharsets.UTF_8)) {
            LineReader lines = new LineReader(reader);

            String line;
            while ((line = lines.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    out.add(line);
                }
            }
        }

        return out.build();
    }
}
