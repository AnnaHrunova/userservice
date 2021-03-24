package com.emailplatform.userservice.service.vo.secret;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserData {

    private String firstName;
    private String middleName;
    private String lastName;
}
