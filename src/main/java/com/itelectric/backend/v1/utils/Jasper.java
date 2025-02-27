package com.itelectric.backend.v1.utils;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Jasper {
    public static byte[] generatePDF(String reportName, Map<String, Object> params, Collection<?> data) throws IOException {
        final String reportPath = "reports/" + reportName + ".jasper";
        String subReportPath = new File(reportPath).getParent();
        String subReportDir = subReportPath + File.separator;
        params.put("SUB_REPORT_DIR", subReportDir);
        ClassPathResource reportSource = new ClassPathResource(reportPath);
        try (InputStream reportStream = reportSource.getInputStream()) {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, params, dataSource);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (IOException ex) {
            throw new IOException("An error occurred while uploading report: " + reportName, ex);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }
}
