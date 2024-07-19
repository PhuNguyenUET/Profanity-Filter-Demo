package com.filter.profanity_filter;

import com.filter.profanity_filter.utility.StringUtil;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.*;

public class ProfanityFilterUtil {
    private final Map<String, Set<String>> dictonariesMap = new HashMap<>();

    private final String folderPath;

    ProfanityFilterUtil(String folderPath) {
        this.folderPath = folderPath;
        reloadAllDictionaries();
    }


    public void reloadAllDictionaries() {
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
            dictonariesMap.put(language, languageProfanitySet);
        } catch (IOException ignored) {
        }
    }
    public String searchAndFilter(String text, String language) {
        if(!dictonariesMap.containsKey(language)) {
            return text;
        }
        StringBuilder filterText = new StringBuilder(text.toLowerCase());
        Set<String> profanitySet = dictonariesMap.get(language);
        for(int i = 0; i < filterText.length(); i++) {
            for(int j = filterText.length(); j > i; j--) {
                String profanitySubstring = filterText.substring(i, j);
                if(profanitySet.contains(profanitySubstring)) {
                    for(int k = i; k < j; k++) {
                        if(filterText.charAt(k) == ' ') {
                            filterText.setCharAt(k, ' ');
                        } else {
                            filterText.setCharAt(k,'*');
                        }
                    }
                    break;
                }
            }
        }
        text = StringUtil.normalizeText(text, filterText.toString());
        return text;
    }
}