package org.closure.laser.service.dto;

public class OffersDTO {

    Long id;
    TripDealDTO tripDealDTO;
    ShipmentDealDTO shipmentDealDTO;
    String status;
    Long senderId;

    public OffersDTO() {}

    public OffersDTO(Long id, TripDealDTO tripDealDTO, ShipmentDealDTO shipmentDealDTO, String status, Long senderId) {
        this.id = id;
        this.tripDealDTO = tripDealDTO;
        this.shipmentDealDTO = shipmentDealDTO;
        this.status = status;
        this.senderId = senderId;
    }

    public TripDealDTO getTripDealDTO() {
        return this.tripDealDTO;
    }

    public void setTripDealDTO(TripDealDTO tripDealDTO) {
        this.tripDealDTO = tripDealDTO;
    }

    public ShipmentDealDTO getShipmentDealDTO() {
        return this.shipmentDealDTO;
    }

    public void setShipmentDealDTO(ShipmentDealDTO shipmentDealDTO) {
        this.shipmentDealDTO = shipmentDealDTO;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
}
