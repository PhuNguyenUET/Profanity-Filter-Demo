package com.filter.profanity_filter;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class ProfanityFilterUtil {
    private final Set<String> profanitySetVi = new HashSet<>();
    private final Set<String> profanitySetEn = new HashSet<>();
    private static volatile ProfanityFilterUtil instance = null;
    private ProfanityFilterUtil() {
        getProfanityListFromFile("vi");
        getProfanityListFromFile("en");
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
                        profanitySetVi.add(line);
                    } else {
                        profanitySetEn.add(line);
                    }
                }
            }
        } catch (IOException ignored) {
        }
    }
    public String searchAndFilter(String text, String language) {
        if(!language.equals("vi") && !language.equals("en")) {
            return text;
        }
        String filterText = text.toLowerCase();
        Set<String> profanitySet = language.equals("vi") ? profanitySetVi : profanitySetEn;
        for(String word : profanitySet) {
            String filteredString = StringUtil.buildFilterString(word);
            filterText = filterText.replaceAll(word, filteredString);
        }
        text = StringUtil.normalizeText(text, filterText);
        return text;
    }
}