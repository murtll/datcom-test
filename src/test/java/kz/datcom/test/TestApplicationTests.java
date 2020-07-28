package kz.datcom.test;

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
    PDFService service;

    @Test
    void testMergePdfs() {

        List<File> list = new ArrayList<>();

        list.add(new File("D:\\murtll\\work\\datcom\\projects\\test\\files\\input\\description.pdf"));
        list.add(new File("D:\\murtll\\work\\datcom\\projects\\test\\files\\input\\agreement.pdf"));

        service.mergePDFs(list);

    }

    @Test
    void testMyInserting() {

        service.addQRsToPDF(new File("D:\\murtll\\work\\datcom\\projects\\test\\files\\output\\output.pdf"), new Date().toString());

    }

}
