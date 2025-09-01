package cass.utils;

import cass.dto.OpportunityDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class CSAHelper {

    public static ByteArrayInputStream
    OpportunitiesToCSV(List<OpportunityDTO> opportunities)  {
        // Define o formato CSV com cabeçalhos específicos
        final CSVFormat format = CSVFormat.DEFAULT.withHeader("ID Proposta", "Cliente", "Preço por Tonelada", "Melhor Cotação de Moeda");

        try (
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);)
        {
            for (OpportunityDTO opps : opportunities){
                List<String> data = Arrays.asList(String.
                        valueOf(opps.getProposalId()), opps.getCustomer(), String.valueOf(opps.getPriceTonne()), String.valueOf(opps.getLastDollarQuotation()));
                csvPrinter.printRecord(data);
            }
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch(IOException e){
            throw new RuntimeException("Falha ao importar dados para arquivo CSV" + e.getMessage());
        }
    }
}
