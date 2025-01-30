package com.georgyorlov.cv.controller;

import com.georgyorlov.cv.config.LocaleProperties;
import com.georgyorlov.cv.config.PdfProperties;
import com.georgyorlov.cv.service.BaseService;
import jakarta.servlet.http.HttpServletRequest;
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

import java.io.IOException;

@RestController
public class BaseController {

    private final LocaleProperties localeProperties;
    private final PdfProperties pdfProperties;
    private final BaseService baseService;
    private final HttpHeaders headers;
    Logger logger = LoggerFactory.getLogger(BaseController.class);

    public BaseController(LocaleProperties localeProperties,
                          PdfProperties pdfProperties,
                          BaseService baseService) {
        this.localeProperties = localeProperties;
        this.pdfProperties = pdfProperties;
        this.baseService = baseService;
        headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=%s".formatted(pdfProperties.getFileName()));
    }

    @GetMapping("/")
    public String index(@RequestHeader("accept-language") String acceptedLang,
                        HttpServletRequest req) throws IOException {
        //log requests
        logger.info("GET / from %s. User-Agent: %s".formatted(req.getRemoteAddr(), req.getHeader(HttpHeaders.USER_AGENT)));
        String preferredLanguage = baseService.parseLanguageFromHeader(acceptedLang);
        return baseService.getHtmlContentByDocType(localeProperties.getLocaleSettings(preferredLanguage), "html");
    }

    @GetMapping("/{locale}")
    public String indexRu(@PathVariable("locale") String locale, HttpServletRequest req) throws IOException {
        //log requests
        logger.info("GET / from %s. User-Agent: %s".formatted(req.getRemoteAddr(), req.getHeader(HttpHeaders.USER_AGENT)));
        return baseService.getHtmlContentByDocType(localeProperties.getLocaleSettings(locale), "html");//localeHandbook.getOrDefault(locale, localeHandbook.get("default"))
    }

    @GetMapping("/{locale}/download")
    public ResponseEntity<Resource> download(@PathVariable("locale") String locale, HttpServletRequest req) throws IOException {
        //log requests
        logger.info("GET /download from %s. User-Agent: %s".formatted(req.getRemoteAddr(), req.getHeader(HttpHeaders.USER_AGENT)));
        String pdfContentInHtml = baseService.getHtmlContentByDocType(localeProperties.getLocaleSettings(locale), "pdf");//localeHandbook.getOrDefault(locale, localeHandbook.get("default"))
        InputStreamResource pdf = baseService.getPdf(pdfContentInHtml, pdfProperties.getFontName(), pdfProperties.getFontFamily());
        return ResponseEntity.ok()
                .headers(headers)
                //.contentLength(pdf.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(pdf);
    }
}
