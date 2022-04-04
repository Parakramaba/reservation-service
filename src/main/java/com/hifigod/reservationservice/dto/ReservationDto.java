package com.hifigod.reservationservice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@ApiModel(description = "Details about the reservation")
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {

    @ApiModelProperty(notes = "Valid user id who make the reservation")
    private String userId;

    @ApiModelProperty(notes = "Valid room id which reserve")
    private String roomId;

    @ApiModelProperty(notes = "List of reservation time")
    private List<ReservationTimeDto> reservationTimes;

}
