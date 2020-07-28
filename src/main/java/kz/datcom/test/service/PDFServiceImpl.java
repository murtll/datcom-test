package kz.datcom.test.service;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * @author murtll
 * This service does all business logic with PDF-files
 * @see kz.datcom.test.service.PDFService
 * */
@Service
public class PDFServiceImpl implements PDFService {
    /**
     * This method merge all PDF-files in one, puts it in file "files/output/output.pdf" in root directory of the project.
     * @param files list of all PDF-files.
     * @return absolute path to output file if execution succeed or error root cause if exception was thrown.
     * */
    @Override
    public String mergePDFs(List<File> files) {

        PDFMergerUtility pdfMerger = new PDFMergerUtility();

        String currentFileName = files.get(0).getName();

        try {
            for (File file : files) {
                currentFileName = file.getName();
                pdfMerger.addSource(file);
            }

            File outputDir = new File("files\\output");

            if (!outputDir.exists()) {
                outputDir.mkdir();
            }

            pdfMerger.setDestinationFileName(outputDir.getAbsolutePath() + "\\output.pdf");

            pdfMerger.mergeDocuments();

        } catch (FileNotFoundException e) {
            System.out.println("[" + getClass() + "]: Cannot find file: " + currentFileName);
            e.printStackTrace();
            return "File not found: " + currentFileName;
        } catch (IOException e) {
            System.out.println("[" + getClass() + "]: Cannot merge files! File list:");
            System.out.println(files);
            e.printStackTrace();
            return "Cannot merge files!";
        }

        return pdfMerger.getDestinationFileName();
    }

    /**
     * This method add to each page of pdf document QR-code with text
     * @param pdf PDF-file where you need to put QR-codes
     * @param qrText text which will be coded into QR-code
     * @return absolute path to the final document or a string describing a problem if exception was thrown
     * */
    @Override
    public String addQRsToPDF(File pdf, String qrText) {

        File qr = QRCode.from(qrText).to(ImageType.JPG).withSize(50, 50).file();

        try {
            PDDocument document = PDDocument.load(pdf);

            for (int i = 0; i < document.getNumberOfPages(); i++) {
                document = PDDocument.load(pdf);

                PDPage page = document.getPage(i);

                PDImageXObject image = PDImageXObject.createFromFile(qr.getAbsolutePath(), document);

                PDPageContentStream stream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, false);

                stream.drawImage(image, 5, 5);

                stream.close();

                document.save(pdf);

                document.close();

            }

            qr.delete();

        } catch (IOException e) {
            System.out.println("[" + getClass() + "]: Cannot add qr code to file" );
            e.printStackTrace();
            return "Cannot add qr code to file";
        }

        return pdf.getAbsolutePath();
    }
}
