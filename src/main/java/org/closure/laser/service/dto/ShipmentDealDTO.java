package org.closure.laser.service.dto;

import java.util.List;
import org.closure.laser.domain.Deal;
import org.closure.laser.domain.Shipment;
import org.closure.laser.domain.UserApplication;

public class ShipmentDealDTO {

    Deal deal;
    List<Shipment> shipments;
    UserApplication owner;

    public ShipmentDealDTO() {}

    public ShipmentDealDTO(Deal deal, List<Shipment> shipments, UserApplication owner) {
        this.deal = deal;
        this.shipments = shipments;
        this.owner = owner;
    }

    public Deal getDeal() {
        return this.deal;
    }

    public void setDeal(Deal deal) {
        this.deal = deal;
    }

    public List<Shipment> getShipments() {
        return this.shipments;
    }

    public void setShipments(List<Shipment> shipments) {
        this.shipments = shipments;
    }

    public UserApplication getOwner() {
        return this.owner;
    }

    public void setOwner(UserApplication owner) {
        this.owner = owner;
    }
}
