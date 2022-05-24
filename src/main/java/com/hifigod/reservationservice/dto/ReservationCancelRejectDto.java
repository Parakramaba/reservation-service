package com.hifigod.reservationservice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel(description = "Details of reservation cancellation/rejection")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ReservationCancelRejectDto {

    @ApiModelProperty(notes = "Valid reservation id")
    private String reservationId;

    @ApiModelProperty(notes = "Cancellation or Rejection message")
    private String message;
}
