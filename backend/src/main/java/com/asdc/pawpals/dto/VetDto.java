package com.asdc.pawpals.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VetDto extends UserDto {
    private String phoneNo;
    private String licenseNumber;
    private String clinicAddress;
    private Integer experience;
    private String qualification;
    private Byte[] clinicUrl;

    private String profileStatus;
}
