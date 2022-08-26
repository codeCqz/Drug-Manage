package com.drug.entity.dto;

import lombok.Data;

@Data
public class UserVerify {
    String username;
    String password;
    String captcha;
}
