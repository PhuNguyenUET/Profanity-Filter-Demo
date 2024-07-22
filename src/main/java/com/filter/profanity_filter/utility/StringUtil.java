package com.filter.profanity_filter.utility;

public class StringUtil {
    public static String buildFilterString(String word) {
        int n = word.length();
        StringBuilder filteredStringBuilder = new StringBuilder();
        for(int i = 0; i < n; i++) {
            if(word.charAt(i) == ' ') {
                filteredStringBuilder.append(' ');
            } else {
                filteredStringBuilder.append('*');
            }
        }
        return filteredStringBuilder.toString();
    }

    public static String normalizeText(String text, String filteredText) {
        StringBuilder normalText = new StringBuilder(text);
        for(int i = 0; i < filteredText.length(); i++) {
            if(filteredText.charAt(i) == '*') {
                normalText.setCharAt(i, '*');
            }
        }
        return normalText.toString();
    }
}
