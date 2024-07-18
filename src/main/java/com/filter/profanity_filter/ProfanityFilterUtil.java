package com.filter.profanity_filter;

import java.io.*;
import java.util.*;

public class ProfanityFilterUtil {
    private final List<String> profanityListVi = new ArrayList<>();
    private final List<String> profanityListEn = new ArrayList<>();

    private final Map<String, List<String>> profanityBreakDownMapVi = new HashMap<>();
    private final Map<String, List<String>> profanityBreakDownMapEn = new HashMap<>();

    private static volatile ProfanityFilterUtil instance = null;

    private ProfanityFilterUtil() {
        getProfanityListFromFile("vi");
        breakDownProfanityList("vi");

        getProfanityListFromFile("en");
        breakDownProfanityList("en");
    }

    public static ProfanityFilterUtil getInstance() {
        if(instance == null) {
            synchronized (ProfanityFilterUtil.class) {
                if(instance == null) {
                    instance = new ProfanityFilterUtil();
                }
            }
        }

        return instance;
    }

    private void getProfanityListFromFile(String language) {
        if(!language.equals("vi") && !language.equals("en")) {
            return;
        }

        try (InputStream in = getClass().getResourceAsStream("/profanity." + language);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("#")) {
                    if(language.equals("vi")) {
                        profanityListVi.add(line);
                    } else {
                        profanityListEn.add(line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void breakDownProfanityList(String language) {
        if(!language.equals("vi") && !language.equals("en")) {
            return;
        }
        List<String> profanityList = language.equals("vi") ? profanityListVi : profanityListEn;
        Map<String, List<String>> profanityBreakDownMap = language.equals("vi") ? profanityBreakDownMapVi : profanityBreakDownMapEn;
        for(String profanity : profanityList) {
            String[] splitProfanity = profanity.split("\\s+");
            for(String split : splitProfanity) {
                if(!profanityBreakDownMap.containsKey(split)) {
                    List<String> profanityContainingWord = new ArrayList<>();
                    profanityContainingWord.add(profanity);
                    profanityBreakDownMap.put(split, profanityContainingWord);
                } else {
                    List<String> profanityContainingWord = profanityBreakDownMap.get(split);
                    profanityContainingWord.add(profanity);
                }
            }
        }
    }

    public String searchAndFilter(String text, String language) {
        if(!language.equals("vi") && !language.equals("en")) {
            return text;
        }
        String filterText = text.toLowerCase();
        Set<String> profanitySet = narrowDownProfanityList(filterText, language);
        for(String word : profanitySet) {
            String filteredString = buildFilterString(word);
            filterText = filterText.replaceAll(word, filteredString);
        }
        text = normalizeText(text, filterText);
        return text;
    }

    private Set<String> narrowDownProfanityList(String text, String language) {
        text = text.replaceAll("\\p{Punct}", "");
        Set<String> profanityNarrow = new HashSet<>();
        String[] splitInputText = text.split("\\s+");

        Map<String, List<String>> profanityBreakDownMap = language.equals("vi") ? profanityBreakDownMapVi : profanityBreakDownMapEn;

        for(String split : splitInputText) {
            List<String> candidateWords = profanityBreakDownMap.get(split);
            if(candidateWords != null) {
                profanityNarrow.addAll(candidateWords);
            }
        }
        return profanityNarrow;
    }

    private String buildFilterString(String word) {
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

    private String normalizeText(String text, String filteredText) {
        StringBuilder normalText = new StringBuilder(text);
        for(int i = 0; i < filteredText.length(); i++) {
            if(filteredText.charAt(i) == '*') {
                normalText.setCharAt(i, '*');
            }
        }
        return normalText.toString();
    }
}
