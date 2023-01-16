package com.sparta.team_1_hyogeunchild.presentation.controller;

import com.sparta.team_1_hyogeunchild.business.dto.CreateResponseDto;
import com.sparta.team_1_hyogeunchild.business.dto.DeleteResponseDto;
import com.sparta.team_1_hyogeunchild.business.dto.LoginResponseDto;
import com.sparta.team_1_hyogeunchild.business.service.UserService;
import com.sparta.team_1_hyogeunchild.presentation.dto.LoginRequestDto;
import com.sparta.team_1_hyogeunchild.presentation.dto.SignUpRequestDto;
import com.sparta.team_1_hyogeunchild.presentation.dto.UserDeleteRequestDto;
import com.sparta.team_1_hyogeunchild.security.jwt.JwtUtil;
import com.sparta.team_1_hyogeunchild.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
//메서드 혹은 클래스 단위로 Mapping을 주어 중복 URL을 공통으로 처리할 수 있다
@RequestMapping("/api/users")
// final이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성해주는 롬복 어노테이션
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    //1.회웝가입
    //POST 메소드는 주로 새로운 리소스를 생성(create)할 때 사용. 서버에 데이터 추가 or 작성시
    @PostMapping("/signup")

    //@RequestBody -> 클라이언트에서 JSON 데이터를 요청 본문에 담아서 서버로 보내면,
    //서버에서는 @RequestBody 어노테이션을 사용하여 HTTP 요청 본문에 담긴 값들을 자바객체로 변환시켜, 객체에 저장한다.
    //@Valid
    //->@RequestBody 어노테이션 옆에 @Valid를 작성하면, RequestBody로 들어오는 객체에 대한 검증을 수행한다.
    // 이 검증의 세부적인 사항은 객체 안에 정의를 해두어야 한다.ex)정규표현식
    public CreateResponseDto signupPage(@RequestBody @Valid SignUpRequestDto signupRequestDto) {
        CreateResponseDto msg =userService.signUp(signupRequestDto);
        return msg;
    }
    //2.로그인
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        //이름과 유저인지 관리자인지 구분한 토큰을 가져오는 부분
        LoginResponseDto msg = userService.login(loginRequestDto);
        //문자열 token에 가져온 정보를 넣어주는 부분
        String token = msg.getMessage();
        //헤더를 통해 토큰을 발급해 주는 부분
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
        return new LoginResponseDto("로그인 되었습니다.");
    }
    //3.유저 삭제
    @DeleteMapping("/delete")
    //@AuthenticationPrincipal -> 세션 정보 UserDetails에 접근할 수 있는 어노테이션
    //현재 로그인한 사용자 객체를 가져오기 위해 필요
    public DeleteResponseDto delete(@RequestBody UserDeleteRequestDto deleteRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.deleteUser(deleteRequestDto, userDetails.getUser());
    }
}