package com.example.zuul.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gxkai on 2018-11-23 9:36 AM
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonPackage {
    private int status;
    private String message;
}
