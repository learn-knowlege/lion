/**
 *   Copyright 2019 Yanzheng (https://github.com/micyo202). All rights reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.lion.common.exception;

import com.lion.common.constant.ResponseCode;
import com.lion.common.result.Result;
import com.lion.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * CustomAuthenticationEntryPoint
 * 自定义未授权的响应处理
 *
 * @author Yanzheng (https://github.com/micyo202)
 * @date 2019/09/26
 */
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        log.error(authException.getMessage());

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        Throwable cause = authException.getCause();
        if (cause instanceof InvalidTokenException) {
            response.getWriter().print(JsonUtil.obj2Json(Result.failure(ResponseCode.UNAUTHORIZED, "无效的 Access Token")));
        } else if (cause instanceof InvalidGrantException) {
            response.getWriter().print(JsonUtil.obj2Json(Result.failure(ResponseCode.UNAUTHORIZED, "无效的 Refresh Token")));
        } else if (cause instanceof AccessDeniedException) {
            response.getWriter().print(JsonUtil.obj2Json(Result.failure(ResponseCode.FORBIDDEN, "权限不足无法访问")));
        } else {
            response.getWriter().print(JsonUtil.obj2Json(Result.failure(ResponseCode.UNAUTHORIZED, "尚未认证无法访问")));
        }

        /*
        if (isAjaxRequest(request)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), authException.getMessage());
        } else {
            response.sendRedirect("/login");
        }
        */

    }

    /*
    private static boolean isAjaxRequest(HttpServletRequest request) {
        if (request.getHeader("accept").indexOf("application/json") > -1
                || (request.getHeader("X-Requested-With") != null
                && request.getHeader("X-Requested-With").equals("XMLHttpRequest"))) {
            return true;
        }
        return false;
    }
    */

}
