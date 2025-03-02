package com.georgyorlov.cv.service;

import com.georgyorlov.cv.properties.CvAppProperties;
import com.georgyorlov.cv.properties.LocaleProperties;
import com.github.rjeschke.txtmark.Processor;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
public class FileService {

    private final RestTemplate restTemplate;
    private final CvAppProperties cvAppProperties;
    Logger logger = LoggerFactory.getLogger(BaseService.class);

    public FileService(CvAppProperties cvAppProperties) {
        this.restTemplate = new RestTemplate();
        this.cvAppProperties = cvAppProperties;
    }

    public byte[] getPdf(String pdfContentInHtml, String fontName, String fontFamily) throws IOException {
        logger.info("Getting pdf content");
        try (ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
            final Document w3cDoc = new W3CDom().fromJsoup(Jsoup.parse(pdfContentInHtml, StandardCharsets.UTF_8.name()));

            final PdfRendererBuilder pdfBuilder = new PdfRendererBuilder();
            pdfBuilder.useFastMode();
            pdfBuilder.withW3cDocument(w3cDoc, "/");
            pdfBuilder.useFont(ResourceUtils.getFile("%s/fonts/%s.ttf".formatted(cvAppProperties.getResourcesPath(), fontName)), fontFamily);
            pdfBuilder.toStream(outStream);

            pdfBuilder.run();

            return outStream.toByteArray();
        }
    }

    @Cacheable("content")
    public String getHtmlContentByDocType(LocaleProperties.LocaleSettings localeSettings, String documentTypePrefix) throws IOException {
        logger.info("Getting content for document type {} and for locale {}", documentTypePrefix, localeSettings.getLocale());
        String htmlTemplate = Files.readString(ResourceUtils.getFile("%s/%s/%s_template.html".formatted(cvAppProperties.getResourcesPath(), documentTypePrefix, localeSettings.getLocale())).toPath(), StandardCharsets.UTF_8);
        return htmlTemplate.replace("${cv}", getCvTextHtml(localeSettings));
    }

    public String getCvTextHtml(LocaleProperties.LocaleSettings localeSettings) throws IOException {
        String cvTextMd = getMdCvTemplateFromCloudOrGetBase(localeSettings);
        return Processor.process(cvTextMd);
    }

    public String getMdCvTemplateFromCloudOrGetBase(LocaleProperties.LocaleSettings localeSettings) throws IOException {
        try {
            return restTemplate.getForObject(localeSettings.getLink(), String.class);
        } catch (Exception ex) {
            logger.error("Error downloading CV template", ex);
            return Files.readString(ResourceUtils.getFile("%s/backup/base-%s-CV-template.md".formatted(cvAppProperties.getResourcesPath(), localeSettings.getLocale())).toPath());
        }
    }
}
