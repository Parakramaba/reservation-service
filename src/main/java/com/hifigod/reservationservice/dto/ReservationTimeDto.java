//package com.hifigod.reservationservice.dto;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//
//@Data
//@ApiModel(description = "Time slots of reservation")
//@AllArgsConstructor
//@NoArgsConstructor
//public class ReservationTimeDto {
//
//    @ApiModelProperty(notes = "Reserved date")
//    private LocalDate reservedDate;
//
//    @ApiModelProperty(notes = "Session type (Morning/Evening)")
//    private String session;
//
//    @ApiModelProperty(notes = "Start time of single slot")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
//    private LocalTime startTime;
//
//}
