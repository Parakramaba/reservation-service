package com.hifigod.reservationservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@ApiModel(description = "Format of the response")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Response {

    @ApiModelProperty(notes = "Http status code")
    private int status;

    private String error;

    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;

    @ApiModelProperty(notes = "Requested data")
    private Object data;
}
