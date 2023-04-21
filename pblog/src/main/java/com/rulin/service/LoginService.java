package com.rulin.service;


import com.rulin.common.ResponseResult;
import com.rulin.dto.LoginDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author CodeScholar
 * @description:
 * @date 2023年4月9日
 */
public interface LoginService {

    Map<String, String> getCode(HttpServletResponse response) throws IOException;


    ResponseResult login(LoginDTO vo);
}
