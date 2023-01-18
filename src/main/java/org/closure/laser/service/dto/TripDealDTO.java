package org.closure.laser.service.dto;

import org.closure.laser.domain.Deal;
import org.closure.laser.domain.Trip;
import org.closure.laser.domain.UserApplication;

public class TripDealDTO {

    Deal deal;
    Trip trip;
    UserApplication deliver;

    public TripDealDTO() {}

    public TripDealDTO(Deal deal, Trip trip, UserApplication deliver) {
        this.deal = deal;
        this.trip = trip;
        this.deliver = deliver;
    }

    public Deal getDeal() {
        return this.deal;
    }

    public void setDeal(Deal deal) {
        this.deal = deal;
    }

    public Trip getTrip() {
        return this.trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public UserApplication getDeliver() {
        return this.deliver;
    }

    public void setDeliver(UserApplication deliver) {
        this.deliver = deliver;
    }
}
