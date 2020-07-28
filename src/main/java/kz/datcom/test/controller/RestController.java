package kz.datcom.test.controller;

import kz.datcom.test.service.PDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author murtll
 * Main controller of the application.
 * Contains just one method mergeAndStampPdfFiles.
 * */
@Controller
public class RestController {

    /**
     * Autowired field - service to do business logic with PDF-files
     * */
    @Autowired
    private PDFService pdfService;

    /**
     * This method get list of PDF-files names from JSON, gives this names to PDFService to do work.
     * In case if one of files are not PDF-file, this file is skipped. List of not PDF files is printing in the result.
     * @param files list of all PDF-files names.
     * @return path to output file if work successfully done, or error cause if some errors occured. If there are no PDF files, returns "No PDF files detected".
     * */
    @PostMapping(value = "/files", consumes = "application/json")
    @ResponseBody
    public String mergeAndStampPdfFiles(@RequestBody List<String> files) {

        StringBuilder result = new StringBuilder("No PDF files detected!");
        String appendingNotPdf = null;
        String appendingNotFound = null;

        if (files.size() > 0) {

            List<File> fileList = new ArrayList<>();
            List<String> notPdfList = new ArrayList<>();
            List<String> notFoundList = new ArrayList<>();

            for (String fileName: files) {

                File tmpFile = new File(fileName);

                if (tmpFile.exists()) {
                    if (fileName.endsWith(".pdf")) {
                        fileList.add(tmpFile);
                    } else {
                        System.out.println("File '" + fileName + "' is not PDF-file!");
                        notPdfList.add(fileName);
                        appendingNotPdf = "Those files are not PDF files so they was ignored: ";
                    }
                } else {
                    System.out.println("File '" + fileName + "' not found!");
                    notFoundList.add(fileName);
                    appendingNotFound = "Those files was not found: ";
                }

            }

            if (fileList.size() > 0) {
                result = new StringBuilder(pdfService.mergePDFs(fileList));

                if (result.toString().endsWith(".pdf")) {
                    result = new StringBuilder(pdfService.addQRsToPDF(new File(result.toString()), new Date().toString()));
                }
            }

            if (appendingNotPdf != null) {
                result.append("\n").append(appendingNotPdf);

                for (String filename : notPdfList) {
                    result.append("\n").append(filename);
                }
            }

            if (appendingNotFound != null) {
                result.append("\n").append(appendingNotFound);

                for (String filename : notFoundList) {
                    result.append("\n").append(filename);
                }
            }

        }

        System.out.println(result);

        return result.toString();
    }

}