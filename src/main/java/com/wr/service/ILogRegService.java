package com.wr.service;

import com.wr.domain.LogRegPojo.RegisterUser;

public interface ILogRegService {


    String login(String username,String password,String code,String uuid);

    boolean register(RegisterUser registerUser);
}
