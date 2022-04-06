package com.hifigod.reservationservice.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel(description = "Details of reservation cancellation/rejection")
@AllArgsConstructor
@NoArgsConstructor
public class ReservationCancelRejectDto {

    private String reservationId;

    private String message;
}