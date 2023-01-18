package org.closure.laser.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.closure.laser.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OffersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Offers.class);
        Offers offers1 = new Offers();
        offers1.setId(1L);
        Offers offers2 = new Offers();
        offers2.setId(offers1.getId());
        assertThat(offers1).isEqualTo(offers2);
        offers2.setId(2L);
        assertThat(offers1).isNotEqualTo(offers2);
        offers1.setId(null);
        assertThat(offers1).isNotEqualTo(offers2);
    }
}
