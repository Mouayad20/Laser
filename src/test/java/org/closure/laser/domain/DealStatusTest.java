package org.closure.laser.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.closure.laser.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DealStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DealStatus.class);
        DealStatus dealStatus1 = new DealStatus();
        dealStatus1.setId(1L);
        DealStatus dealStatus2 = new DealStatus();
        dealStatus2.setId(dealStatus1.getId());
        assertThat(dealStatus1).isEqualTo(dealStatus2);
        dealStatus2.setId(2L);
        assertThat(dealStatus1).isNotEqualTo(dealStatus2);
        dealStatus1.setId(null);
        assertThat(dealStatus1).isNotEqualTo(dealStatus2);
    }
}
