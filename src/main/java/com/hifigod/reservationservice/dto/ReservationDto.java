package com.hifigod.reservationservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@ApiModel(description = "Details of the reservation")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ReservationDto {

    @ApiModelProperty(notes = "Valid user id who make the reservation")
    private String userId;

    @ApiModelProperty(notes = "Valid room id which reserve")
    private String roomId;

//    private LocalDate reservedDate;

    @ApiModelProperty(notes = "Morning or Evening")
    private String session;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

//    @ApiModelProperty(notes = "List of reservation time")
//    private List<ReservationTimeDto> reservationTimes;

}
