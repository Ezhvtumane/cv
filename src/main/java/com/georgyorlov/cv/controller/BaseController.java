package com.georgyorlov.cv.controller;

import com.georgyorlov.cv.properties.LocaleProperties;
import com.georgyorlov.cv.properties.PdfProperties;
import com.georgyorlov.cv.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
public class BaseController {

    private final LocaleProperties localeProperties;
    private final BaseService baseService;
    private final HttpHeaders headers;
    Logger logger = LoggerFactory.getLogger(BaseController.class);

    public BaseController(LocaleProperties localeProperties,
                          PdfProperties pdfProperties,
                          BaseService baseService) {
        this.localeProperties = localeProperties;
        this.baseService = baseService;
        headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=%s".formatted(pdfProperties.getFileName()));
    }

    @GetMapping("/")
    public ResponseEntity<String> index(@RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false) String acceptedLang,
                                        @RequestHeader(value = "X-Real-IP", required = false) String xRealIp,
                                        @RequestHeader(value = "X-Forwarded-For", required = false) String xForwardedFor,
                                        @RequestHeader(value = HttpHeaders.USER_AGENT, required = false) String userAgent) throws IOException {
        logger.info("GET / from %s %s. User-Agent: %s. locale: %s".formatted(xRealIp, xForwardedFor, userAgent, acceptedLang));
        return ResponseEntity.ok(baseService.getIndex(acceptedLang));
    }

    @GetMapping("/{locale}")
    public ResponseEntity<String> indexRu(@PathVariable("locale") String locale,
                                          @RequestHeader(value = "X-Real-IP", required = false) String xRealIp,
                                          @RequestHeader(value = "X-Forwarded-For", required = false) String xForwardedFor,
                                          @RequestHeader(value = HttpHeaders.USER_AGENT, required = false) String userAgent) throws IOException {
        logger.info("GET /%s from %s %s. User-Agent: %s".formatted(locale, xRealIp, xForwardedFor, userAgent));
        if (isValidLocale(locale)) {
            return ResponseEntity
                    .ok()
                    .body(baseService.getIndexByLocale(locale));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{locale}/download")
    public ResponseEntity<Resource> download(@PathVariable("locale") String locale,
                                             @RequestHeader(value = "X-Real-IP", required = false) String xRealIp,
                                             @RequestHeader(value = "X-Forwarded-For", required = false) String xForwardedFor,
                                             @RequestHeader(value = HttpHeaders.USER_AGENT, required = false) String userAgent) throws IOException {
        logger.info("GET /%s/download from %s %s. User-Agent: %s".formatted(locale, xRealIp, xForwardedFor, userAgent));
        if (isValidLocale(locale)) {
            byte[] pdf = baseService.getPdfByLocale(locale);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(new ByteArrayInputStream(pdf)));
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/update-cv")
    public ResponseEntity<Object> updateCvInCache() {
        baseService.updateCvInCache();
        return ResponseEntity.ok().build();
    }

    private boolean isValidLocale(String locale) {
        if (locale == null) return false;
        if (locale.isEmpty()) return false;
        if (locale.length() > 3) return false;

        return localeProperties.getLangs()
                .stream()
                .anyMatch(locale::equals);
    }
}
