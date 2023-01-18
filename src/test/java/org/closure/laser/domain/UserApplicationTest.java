package org.closure.laser.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.closure.laser.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserApplicationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserApplication.class);
        UserApplication userApplication1 = new UserApplication();
        userApplication1.setId(1L);
        UserApplication userApplication2 = new UserApplication();
        userApplication2.setId(userApplication1.getId());
        assertThat(userApplication1).isEqualTo(userApplication2);
        userApplication2.setId(2L);
        assertThat(userApplication1).isNotEqualTo(userApplication2);
        userApplication1.setId(null);
        assertThat(userApplication1).isNotEqualTo(userApplication2);
    }
}
