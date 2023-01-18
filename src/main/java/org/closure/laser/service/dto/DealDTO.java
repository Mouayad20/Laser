package org.closure.laser.service.dto;

import java.util.Set;
import org.closure.laser.domain.Deal;
import org.closure.laser.domain.Shipment;
import org.closure.laser.domain.Trip;

public class DealDTO {

    Deal deal;
    Set<Shipment> shipments;
    Trip trip;

    public DealDTO() {}

    public DealDTO(Deal deal, Set<Shipment> shipments) {
        this.deal = deal;
        this.shipments = shipments;
    }

    public DealDTO(Deal deal, Trip trip) {
        this.deal = deal;
        this.trip = trip;
    }

    public Deal getDeal() {
        return this.deal;
    }

    public void setDeal(Deal deal) {
        this.deal = deal;
    }

    public Set<Shipment> getShipments() {
        return this.shipments;
    }

    public void setShipments(Set<Shipment> shipments) {
        this.shipments = shipments;
    }

    public Trip getTrip() {
        return this.trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }
}
