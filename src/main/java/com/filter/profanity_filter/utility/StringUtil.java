package com.filter.profanity_filter.utility;

public class StringUtil {
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
