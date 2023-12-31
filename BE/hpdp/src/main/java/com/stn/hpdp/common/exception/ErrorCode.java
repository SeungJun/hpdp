package com.stn.hpdp.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST */
    REFRESH_TOKEN_MISMATCH(HttpStatus.BAD_REQUEST, "Refresh Token 정보가 일치하지 않습니다."),
    REFRESH_TOKEN_INVALID(HttpStatus.BAD_REQUEST, "Refresh Token 정보가 유효하지 않습니다."),
    ACCESS_TOKEN_INVALID(HttpStatus.BAD_REQUEST, "Access Token 유효하지 않은 토큰입니다."),
    USER_BAD_REQUEST(HttpStatus.BAD_REQUEST, "해당하는 유저가 존재하지 않습니다."),
    COMPANY_BAD_REQUEST(HttpStatus.BAD_REQUEST, "해당하는 회사는 존재하지 않습니다."),
    INVALID_FIELDS_REQUEST(HttpStatus.BAD_REQUEST, "유효하지 않은 필드입니다."),   // JWTFilterException
    ACCOUNT_PASSWORD_BAD_REQUEST(HttpStatus.BAD_REQUEST, "계좌 비밀번호가 일치하지 않습니다."),
    SCARCE_POINT_BAD_REQUEST(HttpStatus.BAD_REQUEST, "잔액이 부족합니다."),

    /* 401 UNAUTHORIZED */
    UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED, "인증 Token 이 존재하지 않습니다."),     // JWTFilterException
    WRONG_TYPE_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 Access Token 정보입니다."),           // JWTFilterException
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 Access Token 입니다."),               // JWTFilterException
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원하지 않는 Token 입니다."),      // JWTFilterException
    ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "알 수 없는 이유로 요청이 거절되었습니다."), //JWTFilterException
    NOT_LOGIN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요."),

    /* 403 FORBIDDEN : 페이지 접근 거부 */
    NOT_COMPANY_FORBIDDEN(HttpStatus.FORBIDDEN, "펀딩을 등록한 회사만 이용 가능합니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    TRANSFER_FAIL(HttpStatus.NOT_FOUND, "이체에 실패하였습니다."),
    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 기업입니다."),
    CONNECTED_ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "연결된 계좌가 없습니다."),
    WALLET_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자의 지갑이 존재하지 않습니다."),
    FUNDING_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 펀딩입니다."),
    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 쪽지입니다."),
    NEWS_ALARM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 뉴스 알림입니다."),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "데이터가 이미 존재합니다"),
    USER_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 가입되어 있는 유저입니다."),
    ID_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),
    COMPANY_UPDATE_CONFLICT(HttpStatus.CONFLICT, "기업은 정보를 수정할 수 없습니다. 이메일로 문의해주세요."),
    SETTLE_ALREADY_CONFLICT(HttpStatus.CONFLICT, "이미 정산 완료됐습니다."),


    /* 500 INTERNAL_SERVER_ERROR : 서버 내부 로직 에러 */
    CREATE_WALLET_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "사용자의 지갑을 만드는데 실패하였습니다."),
    CREATE_KEYPAIR_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Key Pair를 만드는데 실패하였습니다."),
    CREATE_FUNDING_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "펀딩을 만드는데 실패하였습니다."),
    FUNDING_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "후원에 실패하였습니다."),
    SEND_ETH_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "이더리움 전송에 실패하였습니다."),
    APPROVAL_FUNDING_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "승인에 실패하였습니다."),
    SSE_CONNECTED_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "SSE 연결이 실패하였습니다.");

    private final HttpStatus httpStatus;
    private final String description;
}