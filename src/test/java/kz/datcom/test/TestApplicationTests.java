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

    @Test
    void testQRGen() {
        File file = QRCode.from((new Date()).toString()).file();
        System.out.println(file.getAbsolutePath());
    }

    @Test
    void testFileCopy() {

        OutputStream os = null;
        try {
            os = new FileOutputStream(new File("files\\output\\file.jpg"));
            QRCode.from(new Date().toString()).to(ImageType.JPG).withSize(50, 50).writeTo(os);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void testInserting() throws IOException {
        File file = new File("D:\\murtll\\work\\datcom\\projects\\test\\files\\output\\output.pdf");
        PDDocument doc = PDDocument.load(file);

        //Retrieving the page

        PDPage page = doc.getPage(0);

        //Creating PDImageXObject object
        PDImageXObject pdImage = PDImageXObject.createFromFile("D:\\murtll\\work\\datcom\\projects\\test\\files\\output\\file.jpg",doc);

        //creating the PDPageContentStream object
        PDPageContentStream contents = new PDPageContentStream(doc, page);

        //Drawing the image in the PDF document
        contents.drawImage(pdImage, 70, 250);

        System.out.println("Image inserted");

        //Closing the PDPageContentStream object
        contents.close();

        //Saving the document
        doc.save(file.getAbsolutePath());

        //Closing the document
        doc.close();
    }

    @Autowired
    PDFService service;

    @Test
    void testMyInserting() {

        service.addQRsToPDF(new File("D:\\murtll\\work\\datcom\\projects\\test\\files\\output\\output.pdf"), new Date().toString());

    }

    @Test
    void testMergePdfs() {

        List<File> list = new ArrayList<>();

        list.add(new File("D:\\murtll\\work\\datcom\\projects\\test\\files\\input\\inf.pdf"));
        list.add(new File("D:\\murtll\\work\\datcom\\projects\\test\\files\\input\\mat.pdf"));

        service.mergePDFs(list);

    }

}
