package org.closure.laser.service.dto;

import org.closure.laser.domain.Shipment;
import org.closure.laser.domain.UserApplication;

public class ShipmentDTO {

    private Shipment shipment;
    private UserApplication owner;

    public ShipmentDTO() {}

    public ShipmentDTO(Shipment shipment, UserApplication owner) {
        this.shipment = shipment;
        this.owner = owner;
    }

    public Shipment getShipment() {
        return this.shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public UserApplication getOwner() {
        return this.owner;
    }

    public void setOwner(UserApplication owner) {
        this.owner = owner;
    }
}
