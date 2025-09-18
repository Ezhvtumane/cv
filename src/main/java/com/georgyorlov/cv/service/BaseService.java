package com.georgyorlov.cv.service;

import com.georgyorlov.cv.properties.LocaleProperties;
import com.georgyorlov.cv.properties.NotFoundProperties;
import com.georgyorlov.cv.properties.PdfProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;

@Service
public class BaseService {

    Logger logger = LoggerFactory.getLogger(BaseService.class);

    private final FileService fileService;
    private final LocaleProperties localeProperties;
    private final PdfProperties pdfProperties;
    private final NotFoundProperties notFoundProperties;
    private final Random random = new Random();

    public BaseService(FileService fileService,
                       LocaleProperties localeProperties,
                       PdfProperties pdfProperties,
                       NotFoundProperties notFoundProperties) {
        this.fileService = fileService;
        this.localeProperties = localeProperties;
        this.pdfProperties = pdfProperties;
        this.notFoundProperties = notFoundProperties;
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

    public ResponseEntity<String> getNotFoundOrRandom() {
        if (random.nextInt(0,2) > 0) {
            logger.info("redirect");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(URI.create(notFoundProperties.getSite()));
            return new ResponseEntity<>(httpHeaders, HttpStatus.FOUND);
        } else {
            logger.info("404");
            return ResponseEntity.notFound().build();
        }
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

    public ResponseEntity<byte[]> getFavicon() {
        try {
            Path path = ResourceUtils.getFile("classpath:favicon.ico").toPath();
            byte[] bytes = Files.readAllBytes(path);
            return ResponseEntity.ok(bytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
