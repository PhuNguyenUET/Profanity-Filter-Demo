package com.filter.profanity_filter;

import com.filter.profanity_filter.utility.StringUtil;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.*;

public class ProfanityFilterUtil {
    private final Map<String, Set<String>> dictionariesMap = new HashMap<>();

    private static volatile ProfanityFilterUtil instance = null;
    private ProfanityFilterUtil() {}

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


    public synchronized void reloadAllDictionaries(String folderPath) {
        dictionariesMap.clear();
        final File folder = new File(folderPath);
        if (folder.listFiles() == null) {
            return;
        }
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (!fileEntry.isDirectory()) {
                getProfanityListFromFile(fileEntry.getPath());
            }
        }

    }

    private void getProfanityListFromFile(String filePath) {
        String language = FilenameUtils.getExtension(filePath);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            Set<String> languageProfanitySet = new HashSet<>();
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("#")) {
                    languageProfanitySet.add(line);
                }
            }
            dictionariesMap.put(language, languageProfanitySet);
        } catch (IOException ignored) {
        }
    }

    public String searchAndFilter(String text, String language) {
        if (!dictionariesMap.containsKey(language)) {
            return text;
        }
        String filterText = text.toLowerCase();
        Set<String> profanitySet = dictionariesMap.get(language);
        for (String word : profanitySet) {
            String filteredString = StringUtil.buildFilterString(word);
            filterText = filterText.replaceAll(word, filteredString);
        }
        text = StringUtil.normalizeText(text, filterText);
        return text;
    }
}