package com.hifigod.reservationservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

//    private LocalDate reservedDate;

    private String session;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

//    @ApiModelProperty(notes = "List of reservation time")
//    private List<ReservationTimeDto> reservationTimes;

}
