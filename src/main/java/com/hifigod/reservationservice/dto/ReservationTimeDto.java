package com.hifigod.reservationservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@ApiModel(description = "Time slots of reservation")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ReservationTimeDto {

    @ApiModelProperty(notes = "Session (Morning/Evening)")
    private String session;

//    @ApiModelProperty(notes = "Date of the reservation in format of yyyy-MM-dd")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    private LocalDate reservedDate;

    @ApiModelProperty(notes = "Start time in format of yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @ApiModelProperty(notes = "End time in format of yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
