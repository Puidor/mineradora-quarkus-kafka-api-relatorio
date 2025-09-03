package cass.message;

import cass.dto.ProposalDTO;
import cass.dto.QuotationDTO;
import cass.service.OpportunityService;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class kafkaEvent {

    private final Logger LOG = LoggerFactory.getLogger(kafkaEvent.class);

    @Inject
    OpportunityService opportunityService;

    @Incoming("proposal")
    @Transactional
    public void receiveProposal(ProposalDTO proposal){
        LOG.info("-- Recebendo nova proposta do Tópico Kafka --");
        opportunityService.buildOpportunity(proposal);
    }

    @Incoming("quotation")
    @Blocking
    public void receiveQuotation(QuotationDTO quotation){
        LOG.info("-- Recebendo nova cotação de moeda do Tópico Kafka --");
        opportunityService.saveQuotation(quotation);
    }
}
