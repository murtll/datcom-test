package kz.datcom.test.service;

import java.io.File;
import java.util.List;

/**
 * @author murtll
 * This service does all business logic with PDF-files
 * @see kz.datcom.test.service.PDFServiceImpl
 * */
public interface PDFService {

    /**
     * This method merge all PDF-files in one, puts it in file "files/output/output.pdf" in root directory of the project.
     * @param files list of all PDF-files.
     * @return absolute path to output file if execution succeed or error root cause if exception was thrown.
     * */
    String mergePDFs(List<File> files);

    /**
     * This method add to each page of pdf document QR-code with text
     * @param pdf PDF-file where you need to put QR-codes
     * @param qrText text which will be coded into QR-code
     * @return absolute path to the final document or a string describing a problem if exception was thrown
     * */
    String addQRsToPDF(File pdf, String qrText);

}
