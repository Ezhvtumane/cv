package com.georgyorlov.cv.service;

import com.georgyorlov.cv.properties.LocaleProperties;
import com.georgyorlov.cv.properties.PdfProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

@Service
public class BaseService {

    Logger logger = LoggerFactory.getLogger(BaseService.class);

    private final FileService fileService;
    private final LocaleProperties localeProperties;
    private final PdfProperties pdfProperties;

    public BaseService(FileService fileService,
                       LocaleProperties localeProperties,
                       PdfProperties pdfProperties) {
        this.fileService = fileService;
        this.localeProperties = localeProperties;
        this.pdfProperties = pdfProperties;
    }

    public static String parseLanguageFromHeader(String header) {
        return Optional.ofNullable(header).
                map(h -> Locale.LanguageRange
                        .parse(h)
                        .getFirst()
                        .getRange()
                        .substring(0, 2))
                .orElse("");
    }

    public String getIndex(String acceptedLang) throws IOException {
        return fileService.getHtmlContentByDocType(localeProperties.getLocaleSettings(parseLanguageFromHeader(acceptedLang)), "html");
    }

    public String getIndexByLocale(String locale) throws IOException {
        return fileService.getHtmlContentByDocType(localeProperties.getLocaleSettings(locale), "html");
    }

    @Cacheable("pdf")
    public byte[] getPdfByLocale(String locale) throws IOException {
        String pdfContentInHtml = fileService.getHtmlContentByDocType(localeProperties.getLocaleSettings(locale), "pdf");
        return fileService.getPdf(pdfContentInHtml, pdfProperties.getFontName(), pdfProperties.getFontFamily());
    }

    @CacheEvict(value = {"content", "pdf"}, allEntries = true)
    @Scheduled(fixedRate = 86_400_000)//one per day
    public void updateCvInCache() {
        logger.info("Cache was evicted");
    }
}
