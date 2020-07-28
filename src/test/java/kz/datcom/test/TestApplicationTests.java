package kz.datcom.test;

import kz.datcom.test.controller.RestController;
import kz.datcom.test.service.PDFService;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class TestApplicationTests {
    @Autowired
    private PDFService service;

    @Autowired
    private RestController controller;

    @Test
    void testMergePdfs() {

        List<File> list = new ArrayList<>();

        list.add(new File("files\\input\\description.pdf"));
        list.add(new File("files\\input\\agreement.pdf"));

        String result = service.mergePDFs(list);

        assert result.endsWith("files\\output\\output.pdf");
        System.out.println("Success!");
    }

    @Test
    void testMyInserting() {

        String result = service.addQRsToPDF(new File("files\\output\\output.pdf"), new Date().toString());

        assert result.endsWith("files\\output\\output.pdf");

        System.out.println("Success!");
    }

    @Test
    void allTest() {

        List<String> list = new ArrayList<>();

        list.add("files\\input\\description.pdf");
        list.add("files\\input\\agreement.pdf");

        String result = controller.mergeAndStampPdfFiles(list);

        assert result.contains("files\\output\\output.pdf");
    }

    @Test
    void allTestErrorNotFound() {

        List<String> list = new ArrayList<>();

        list.add("files\\input\\are.pdf");
        list.add("files\\input\\non.pdf");

        String result = controller.mergeAndStampPdfFiles(list);

        assert result.equals("No PDF files detected!\n" +
                "Those files was not found: \n" +
                "files\\input\\are.pdf\n" +
                "files\\input\\non.pdf");
    }

    @Test
    void allTestErrorNotPdf() {

        List<String> list = new ArrayList<>();

        list.add("src\\main\\java\\kz\\datcom\\test\\service\\PDFService.java");
        list.add("src\\main\\java\\kz\\datcom\\test\\controller\\RestController.java");

        String result = controller.mergeAndStampPdfFiles(list);

        assert result.equals("No PDF files detected!\n" +
                "Those files are not PDF files so they was ignored: \n" +
                "src\\main\\java\\kz\\datcom\\test\\service\\PDFService.java\n" +
                "src\\main\\java\\kz\\datcom\\test\\controller\\RestController.java");
    }

}
