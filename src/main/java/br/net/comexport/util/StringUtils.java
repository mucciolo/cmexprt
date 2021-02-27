package br.net.comexport.util;

import static java.lang.System.lineSeparator;

public final class StringUtils {

    public static String extractFirstLine(final String s) {
        return s.split(lineSeparator())[0];
    }
}