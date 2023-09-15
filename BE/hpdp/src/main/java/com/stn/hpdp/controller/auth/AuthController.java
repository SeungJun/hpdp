package com.stn.hpdp.controller.auth;

import com.stn.hpdp.common.ApiResponse;
import com.stn.hpdp.common.exception.CustomException;
import com.stn.hpdp.common.jwt.JwtTokenProvider;
import com.stn.hpdp.controller.auth.Request.CompanySignUpReq;
import com.stn.hpdp.controller.auth.Request.ReissueReq;
import com.stn.hpdp.controller.auth.Request.SignInReq;
import com.stn.hpdp.controller.auth.Request.SignUpReq;
import com.stn.hpdp.service.auth.AuthService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.stn.hpdp.common.exception.ErrorCode.INVALID_FIELDS_REQUEST;
import static com.stn.hpdp.common.util.LogCurrent.*;
import static com.stn.hpdp.common.util.LogCurrent.END;
@Slf4j
@Api
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    @PostMapping // 개인 회원가입
    public ApiResponse<Object> MemberSignUp(@Validated @RequestBody SignUpReq signUpReq, Errors errors) {
        // validation check
        log.info(logCurrent(getClassName(), getMethodName(), START));
        if (errors.hasErrors()) {
            errors.getFieldErrors().forEach(e-> {
                System.out.println("message : " + e.getDefaultMessage());
            });
            log.info(logCurrent(getClassName(), getMethodName(), END));
            throw new CustomException(INVALID_FIELDS_REQUEST);
        }
        log.info(logCurrent(getClassName(), getMethodName(), END));
        return authService.memberSignUp(signUpReq);
    }

    @PostMapping("/company") // 기업 회원가입
    public ApiResponse<Object> CompanysignUp(@Validated @RequestBody CompanySignUpReq signUpReq, Errors errors) {
        // validation check
        log.info(logCurrent(getClassName(), getMethodName(), START));
        if (errors.hasErrors()) {
            errors.getFieldErrors().forEach(e-> {
                System.out.println("message : " + e.getDefaultMessage());
            });
            log.info(logCurrent(getClassName(), getMethodName(), END));
            throw new CustomException(INVALID_FIELDS_REQUEST);
        }
        log.info(logCurrent(getClassName(), getMethodName(), END));
        return authService.CompanySignUp(signUpReq);
    }

    @PostMapping("/login") // 로그인
    public ApiResponse<Object> signIn(@Validated @RequestBody SignInReq signInReq, HttpServletResponse response, Errors errors
    , @RequestParam(name = "type", required = false, defaultValue = "0") int type) {
        // validation check
        log.info(logCurrent(getClassName(), getMethodName(), START));

        if (errors.hasErrors()) {
            errors.getFieldErrors().forEach(e-> {
                System.out.println("message : " + e.getDefaultMessage());
            });
            log.info(logCurrent(getClassName(), getMethodName(), END));
            throw new CustomException(INVALID_FIELDS_REQUEST);
        }

        //todo : type = 0 Memebr / type = 1 Company
        log.info(logCurrent(getClassName(), getMethodName(), END));
        return authService.signIn(signInReq, response);
    }

    @GetMapping("/check/{loginId}") // 아이디 중복 체크
    public ApiResponse<Object> checkLoginId(@PathVariable("loginId") String loginId) {
        return authService.checkLoginId(loginId);
    }

    @PostMapping("/regenerate") // Access Token 재발굽
    public ApiResponse<Object> regenerate(@Validated @RequestBody ReissueReq reissueReq, HttpServletResponse response, Errors errors) {
        // validation check
        log.info(logCurrent(getClassName(), getMethodName(), START));
        if (errors.hasErrors()) {
            throw new CustomException(INVALID_FIELDS_REQUEST);
        }
        log.info(logCurrent(getClassName(), getMethodName(), END));
        return authService.regenerate(reissueReq, response);
    }

    @PostMapping("/logout") // 로그아웃
    public ApiResponse<Object> signOut(HttpServletRequest request) {
        log.info(logCurrent(getClassName(), getMethodName(), START));
        log.info(logCurrent(getClassName(), getMethodName(), END));
        return authService.signOut(request);
    }

    @GetMapping("/authority") // admin 권한 추가
    public ApiResponse<Object> authority() {
        log.info("ADD ROLE_ADMIN");
        return authService.authority();
    }
}
