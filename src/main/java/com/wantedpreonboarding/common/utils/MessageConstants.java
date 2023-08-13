package com.wantedpreonboarding.common.utils;

public class MessageConstants {
    private MessageConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String EXPIRED_JWT = "만료된 토큰입니다.";
    public static final String INVALID_JWT = "유효하지 않은 토큰입니다.";
    public static final String REQUIRED_EMAIL = "이메일은 필수 입력 값입니다.";
    public static final String REQUIRED_PW = "비밀번호는 필수 입력 값입니다.";
    public static final String REQUIRED_TITLE = "제목은 필수 입력 값입니다.";
    public static final String REQUIRED_CONTENT = "내용은 필수 입력 값입니다.";
    public static final String INVALID_USER_ID = "등록되지 않은 사용자 입니다.";
    public static final String INVALID_EMAIL_PW = "아이디 혹은 비밀번호를 다시 입력해주세요.";
    public static final String TITLE_TEXT_MAX_200 = "제목은 200자 미만으로 입력 해주세요.";
    public static final String COMPLETION_POST_SAVED = "게시글을 저장하였습니다.";
    public static final String COMPLETION_POST_UPDATE = "게시글을 수정하였습니다.";
    public static final String COMPLETION_POST_DELETE = "게시글을 삭제하였습니다.";
    public static final String INVALID_POST_ID = "유효하지 않은 게시글 고유 번호입니다.";
    public static final String NO_UPDATE_PERMISSION_POST = "게시글을 수정할 수 있는 권한이 없습니다.";
    public static final String NO_DELETE_PERMISSION_POST = "게시글을 삭제할 수 있는 권한이 없습니다.";
}
