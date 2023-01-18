package org.closure.laser.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.closure.laser.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountProviderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountProvider.class);
        AccountProvider accountProvider1 = new AccountProvider();
        accountProvider1.setId(1L);
        AccountProvider accountProvider2 = new AccountProvider();
        accountProvider2.setId(accountProvider1.getId());
        assertThat(accountProvider1).isEqualTo(accountProvider2);
        accountProvider2.setId(2L);
        assertThat(accountProvider1).isNotEqualTo(accountProvider2);
        accountProvider1.setId(null);
        assertThat(accountProvider1).isNotEqualTo(accountProvider2);
    }
}
