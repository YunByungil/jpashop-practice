package jpapractice.practice.service;

import jpapractice.practice.domain.Member;
import jpapractice.practice.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Test
    void 회원가입() {
        // given
        Member member1 = new Member();
        member1.setName("Kim");

        // when
        Long saveId = memberService.join(member1);


        // then
        assertThat("Kim").isEqualTo(member1.getName());
        assertThat(member1.getId()).isEqualTo(saveId);
        assertThat(member1.getName()).isEqualTo(memberService.findOne(saveId).getName());
    }

    @Test
    void 중복_회원_예외() {
        // given
        Member member1 = new Member();
        member1.setName("Kim");

        Member member2 = new Member();
        member2.setName("Kim");

        // when
        memberService.join(member1);

        // then
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertThat("이미 존재하는 회원입니다.").isEqualTo(ex.getMessage());
    }

}