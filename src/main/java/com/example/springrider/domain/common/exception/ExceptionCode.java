package com.example.springrider.domain.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    // 인증/인가
    AUTH_EXCEPTION("인증인가 예외 메세지"),
    PASSWORD_NOT_MATCH("비밀번호가 일치하지 않습니다."),

    // 회원
    USER_EXCEPTION("회원 예외 메세지"),
    EMAIL_ALREADY_USED("이미 사용 중인 이메일입니다."),
    EMAIL_NOT_FOUND("이메일이 존재하지 않습니다."),
    USER_NOT_FOUND("임시로 만든 유저낫빠운드~"),

    // 가게
    STORE_NOT_FOUND("존재하지 않는 가게입니다."), // 존재하지 않는 가게 조회 시
    STORE_ALREADY_CLOSED("이미 폐업한 가게입니다."), //  이미 폐업한 가게를 다시 폐업할려고 할 때
    STORE_LIMIT_EXCEEDED("사장님은 가게를 최대 3개까지 운영할 수 있습니다."),   // 영업 중인 가게 수가 3개 이상일 때
    STORE_OWNER_ONLY("가게 사장님만 가게를 생성할 수 있습니다."), // 일반 유저가 가게 생성 시도할 때
    STORE_INVALID_TIME("가게 오픈/마감 시간이 올바르지 않습니다."), // 오픈 시간이 마감 시간보다 늦거나, 마감 시간이 오픈 시간보다 빠를 때
    STORE_NOT_ACTIVE("현재 영업 중이 아닌 가게입니다."), // 폐업 상태인 가게를 조회할 때
    STORE_ACCESS_DENIED("해당 가게에 대한 접근 권한이 없습니다."), // 현재 로그인 된 유저가 사장님 권한이 없는 유저일 때
    STORE_USER_MISMATCH("해당 가게의 소유주가 아닙니다."), // 사장님 권한이 있는 유저지만 타인의 가게를 수정 시도할 때
    STORE_INVALID_STATUS_CHANGE("현재 가게 상태에서는 요청한 상태로 변경할 수 없습니다."),

    // 메뉴
    MENU_EXCEPTION("메뉴 예외 메세지"),

    // 주문
    ORDER_EXCEPTION("주문 예외 메세지"),

    // 리뷰
    REVIEW_EXCEPTION("리뷰 예외 메세지");

    private final String message;

}
