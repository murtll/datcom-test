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
     * In case if one of files are not PDF-file, this file is skipped.
     * @param files list of all PDF-files names.
     * @return Method return path to output file if work successfully done, "Fail!" if some errors occured.
     * */
    @PostMapping(value = "/files", consumes = "application/json")
    @ResponseBody
    public String mergeAndStampPdfFiles(@RequestBody List<String> files) {

        String result;

        List<File> fileList = new ArrayList<>();

        for (String fileName: files) {

            System.out.println(fileName);

            if (fileName.endsWith(".pdf")) {
                fileList.add(new File(fileName));
            } else {
                System.out.println("File '" + fileName + "' is not PDF-file!");
            }
        }

        result = pdfService.mergePDFs(fileList);

        if (result.endsWith(".pdf")) {
            result = pdfService.addQRsToPDF(new File(result), new Date().toString());
        }

        System.out.println(result);

        return result;
    }

}


