package com.app.positionback.controller.member;

import com.app.positionback.domain.corporation.CorporationDTO;
import com.app.positionback.domain.corporation.CorporationVO;
import com.app.positionback.domain.member.MemberDTO;
import com.app.positionback.domain.member.MemberVO;
import com.app.positionback.exception.LoginFailException;
import com.app.positionback.service.member.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalTime;
import java.util.Optional;

@Controller
// 최상위 경로를 설정하여 컨트롤러를 구분한다.
//@RequestMapping("/member/*")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/login/login-combine")
    //    @RequestParam(required = false)
//    전달받은 데이터가 null일 경우 required의 default값이 true이기 때문에,
//    NPE가 발생할 수 있다. 이를 필수가 아닌 선택으로(null 허용) 변경하고 싶다면,
//    required 설정을 false로 지정한다.
    public void goToLoginForm(@RequestParam(required = false) Boolean status, MemberDTO memberDTO, HttpServletRequest request, Model model) {
        log.info("status: {}", status); //null 출력됨

        //        쿠키 조회
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                log.info(cookies[i].getName());
                //            save라는 key가 있다면,
                if (cookies[i].getName().equals("save")) {
                    //                해당 value를 화면으로 보낸다.
                    model.addAttribute("save", cookies[i].getValue());

                }
                if (cookies[i].getName().equals("memberEmail")) {
                    memberDTO.setMemberEmail(cookies[i].getValue());
                } else if (cookies[i].getName().equals("corporationEmail")) {
                    memberDTO.setCorporationEmail(cookies[i].getValue());
                }

            }
        }

    }
    //**************************************************************************************
    //memberDTO로 corporation의 정보를 다 받습니다.
    // 아이디와 이메일을 넣었을때, 맞는 아이디와 비번일 경우에도 loginFailException으로 넘어갑니다.ㅜㅜ
    //**************************************************************************************
    @PostMapping("/login/login-combine")
//    HttpSession
//    서버의 Session영역을 관리해주는 객체이다.
//    Spring이 해당 객체를 주입해준다.
    public RedirectView login(MemberDTO memberDTO, String save, HttpSession session, HttpServletResponse response){

//        // 로그인 시도
        //기업 로그인인지 회원 로그인인지
        Optional<MemberVO> foundMember = memberService.loginAsMember(memberDTO);
        Optional<CorporationVO> foundCorporation = memberService.loginAsCorporation(memberDTO);

////        null이 아니면 단일 객체 리턴, null이면 예외 발생
        MemberVO memberVO = foundMember.orElseThrow(() -> {throw new LoginFailException("(" + LocalTime.now() + ")로그인 실패");});
        CorporationVO corporationVO = foundCorporation.orElseThrow(() -> {throw new LoginFailException("(" + LocalTime.now() + ")로그인 실패");});
////        id만 담아놓으면 사용할 때마다 SELECT 쿼리를 발생시켜야 한다(싫어!)
////        session.setAttribute("memberId", memberVO.getId());
////        전체 정보를 담아놓기 때문에 쿼리를 따로 발생시킬 필요 없다(좋아!)
//        session.setAttribute("member", memberVO);

        // MemberVO에서 MemberDTO로 변환

        memberDTO.setId(foundMember.get().getId());
        memberDTO.setMemberName(foundMember.get().getMemberName());
        memberDTO.setMemberEmail(foundMember.get().getMemberEmail());
        memberDTO.setMemberPassword(foundMember.get().getMemberPassword());
        memberDTO.setMemberAddress(foundMember.get().getMemberAddress());
        memberDTO.setMemberAddressDetail(foundMember.get().getMemberAddressDetail());
        memberDTO.setMemberNickname(foundMember.get().getMemberNickname());
        memberDTO.setMemberStatus(foundMember.get().getMemberStatus());
        memberDTO.setMemberType(foundMember.get().getMemberType());
        memberDTO.setMemberWarningCount(foundMember.get().getMemberWarningCount());
        memberDTO.setCorporationName(foundCorporation.get().getCorporationName());
        memberDTO.setCorporationAddress(foundCorporation.get().getCorporationAddress());
        memberDTO.setCorporationAddressDetail(foundCorporation.get().getCorporationAddressDetail());
        memberDTO.setCorporationBusiness(foundCorporation.get().getCorporationBusiness());
        memberDTO.setCorporationType(foundCorporation.get().getCorporationType());
        memberDTO.setCorporationOwner(foundCorporation.get().getCorporationOwner());
        memberDTO.setCorporationEmail(foundCorporation.get().getCorporationEmail());
        memberDTO.setCorporationPassword(foundCorporation.get().getCorporationPassword());
        memberDTO.setCorporationHomepage(foundCorporation.get().getCorporationHomepage());
        memberDTO.setCorporationGen(foundCorporation.get().getCorporationGen());
        memberDTO.setCorporationSales(foundCorporation.get().getCorporationSales());

        memberDTO.setMemberStatus("활동 회원");
        memberDTO.setMemberType("포지셔너");

        // 필요에 따라 추가적인 필드 설정
        log.info("memberDTO: {}", memberDTO);
        // 세션에 memberDTO 저장
        session.setAttribute("memberDTO", memberDTO);


//        화면에서 아이디 저장을 선택했다면 null이 아니다.
        if(save != null){

            //기업 회원인지 일반 회원인지 나눠줘야됩니다!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


//            쿠키 생성, 저장
            Cookie saveCookie = new Cookie("save", save);
            //일반 회원
            Cookie memberEmailCookie = new Cookie("memberEmail", memberDTO.getMemberEmail());
            //기업 회원
            Cookie corporationEmailCookie = new Cookie("corporationEmail",memberDTO.getCorporationEmail());

//            -1: 쿠키 계속 유지
            saveCookie.setMaxAge(-1);
            memberEmailCookie.setMaxAge(-1);

            response.addCookie(saveCookie);
            //일반 회원
            response.addCookie(memberEmailCookie);
            //기업 회원
            response.addCookie(corporationEmailCookie);

        }else{
//            쿠키 삭제
            Cookie saveCookie = new Cookie("save", null);
            Cookie memberEmailCookie = new Cookie("memberEmail", null);
            Cookie corporationEmailCookie = new Cookie("corporationEmail", null);

            saveCookie.setMaxAge(0);
            memberEmailCookie.setMaxAge(0);

            response.addCookie(saveCookie);
            response.addCookie(memberEmailCookie);
            response.addCookie(corporationEmailCookie);
        }

        log.info("로그인 성공: {}", memberVO);
        log.info("로그인 성공: {}", corporationVO);
        log.info("로그인 성공: {}", memberDTO);

        //일반 회원일떄,
        return new RedirectView( "/main/body");
        //기업 회원일때는 corporation/login-corporation-main으로 return
    }

    //    로그 아웃
    @GetMapping("logout")
    public RedirectView logout(HttpSession session){
        session.invalidate();
        return new RedirectView("/login/login-combine");
    }

    //    메인페이지 이동
    @GetMapping("/main/body")
    public  void  goToWebMain(MemberDTO memberDTO,HttpSession session){
        log.info("메인 페이지 들어옴");
        session.invalidate();
    }

    //    비밀번호 찾기 페이지 이동
    @GetMapping("/password-forgotten/forgotten-main")
    public  void  goToPasswordForgotten(MemberDTO memberDTO,HttpSession session){
        log.info("비번 찾기 페이지 들어옴");

    }

    @GetMapping("/forgitteb-main/member-forgotten")
    public void goToMemberForgotten(MemberDTO memberDTO,HttpSession session){
        log.info("멤버 비번 찾기 페이지 들어옴");
    }

    @GetMapping("/forgitteb-main/corporation-forgotten")
    public void goToCorporationForgotten(MemberDTO memberDTO,HttpSession session){
        log.info("기업 비번 찾기 페이지 들어옴");
    }

//    @GetMapping("/main/body")
//    public String goToMainPage(MemberDTO memberDTO){
//        return "/main/body";
//    }
}
