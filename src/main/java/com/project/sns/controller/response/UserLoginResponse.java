package com.project.sns.controller.response;

import com.project.sns.model.User;
import com.project.sns.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserLoginResponse {

    private String token;

}
