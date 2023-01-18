package org.closure.laser.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.closure.laser.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DealTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Deal.class);
        Deal deal1 = new Deal();
        deal1.setId(1L);
        Deal deal2 = new Deal();
        deal2.setId(deal1.getId());
        assertThat(deal1).isEqualTo(deal2);
        deal2.setId(2L);
        assertThat(deal1).isNotEqualTo(deal2);
        deal1.setId(null);
        assertThat(deal1).isNotEqualTo(deal2);
    }
}
