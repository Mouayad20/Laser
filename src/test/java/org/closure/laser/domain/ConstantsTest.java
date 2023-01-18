package org.closure.laser.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.closure.laser.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConstantsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Constants.class);
        Constants constants1 = new Constants();
        constants1.setId(1L);
        Constants constants2 = new Constants();
        constants2.setId(constants1.getId());
        assertThat(constants1).isEqualTo(constants2);
        constants2.setId(2L);
        assertThat(constants1).isNotEqualTo(constants2);
        constants1.setId(null);
        assertThat(constants1).isNotEqualTo(constants2);
    }
}
