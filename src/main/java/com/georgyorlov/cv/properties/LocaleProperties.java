package com.georgyorlov.cv.properties;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "locales")
public class LocaleProperties {

    private final Map<String, LocaleSettings> localeSettingsMap = new HashMap<>();
    Logger logger = LoggerFactory.getLogger(LocaleProperties.class);
    private List<String> langs;
    private List<String> links;
    private String defaultLang;
    private String defaultLink;

    @PostConstruct
    void init() {
        logger.info("Initializing locale properties");
        if (langs.size() != links.size()) {
            throw new IllegalArgumentException("langs and links must have the same length");
        }
        for (int i = 0; i < langs.size(); i++) {
            localeSettingsMap.put(langs.get(i), new LocaleSettings(langs.get(i), links.get(i)));
            logger.info("Added language '{}' & link '{}' to language map", langs.get(i), links.get(i));
        }
        localeSettingsMap.put("default", new LocaleSettings(defaultLang, defaultLink));
        logger.info("Added default language '{}' & link '{}' to language map", defaultLang, defaultLink);
    }

    public LocaleSettings getLocaleSettings(String lang) {
        return localeSettingsMap.getOrDefault(lang, localeSettingsMap.get("default"));
    }

    public Map<String, LocaleSettings> getLocaleSettingsMap() {
        return localeSettingsMap;
    }

    public void setLangs(List<String> langs) {
        this.langs = langs;
    }

    public List<String> getLangs() {
        return this.langs;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public void setDefaultLang(String defaultLang) {
        this.defaultLang = defaultLang;
    }

    public void setDefaultLink(String defaultLink) {
        this.defaultLink = defaultLink;
    }

    public static class LocaleSettings {
        private String locale;
        private String link;

        public LocaleSettings(String locale, String link) {
            this.locale = locale;
            this.link = link;
        }

        public String getLocale() {
            return locale;
        }

        public String getLink() {
            return link;
        }
    }
}