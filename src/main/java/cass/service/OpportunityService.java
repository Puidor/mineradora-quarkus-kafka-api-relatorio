package cass.service;

import cass.dto.OpportunityDTO;
import cass.dto.ProposalDTO;
import cass.dto.QuotationDTO;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public interface OpportunityService {

    void buildOpportunity(ProposalDTO proposal);

    void saveQuotation(QuotationDTO quotation);

    List<OpportunityDTO> generateOpportunityData();
}
