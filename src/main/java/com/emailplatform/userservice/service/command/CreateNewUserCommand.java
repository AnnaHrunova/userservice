package com.emailplatform.userservice.service.command;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateNewUserCommand {

    private String firstName;
    private String lastName;
    private String middleName;
    private List<String> emails;
}
