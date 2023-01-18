package org.closure.laser.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.closure.laser.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShipmentTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentType.class);
        ShipmentType shipmentType1 = new ShipmentType();
        shipmentType1.setId(1L);
        ShipmentType shipmentType2 = new ShipmentType();
        shipmentType2.setId(shipmentType1.getId());
        assertThat(shipmentType1).isEqualTo(shipmentType2);
        shipmentType2.setId(2L);
        assertThat(shipmentType1).isNotEqualTo(shipmentType2);
        shipmentType1.setId(null);
        assertThat(shipmentType1).isNotEqualTo(shipmentType2);
    }
}
