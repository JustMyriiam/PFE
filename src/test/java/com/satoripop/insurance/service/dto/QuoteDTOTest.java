package com.satoripop.insurance.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.insurance.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class QuoteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuoteDTO.class);
        QuoteDTO quoteDTO1 = new QuoteDTO();
        quoteDTO1.setId(UUID.randomUUID());
        QuoteDTO quoteDTO2 = new QuoteDTO();
        assertThat(quoteDTO1).isNotEqualTo(quoteDTO2);
        quoteDTO2.setId(quoteDTO1.getId());
        assertThat(quoteDTO1).isEqualTo(quoteDTO2);
        quoteDTO2.setId(UUID.randomUUID());
        assertThat(quoteDTO1).isNotEqualTo(quoteDTO2);
        quoteDTO1.setId(null);
        assertThat(quoteDTO1).isNotEqualTo(quoteDTO2);
    }
}
