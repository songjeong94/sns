package com.project.sns.service;

import com.project.sns.exception.ErrorCode;
import com.project.sns.exception.SnsApplicationException;
import com.project.sns.fixture.UserEntityFixture;
import com.project.sns.model.entity.UserEntity;
import com.project.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserEntityRepository userEntityRepository;

    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    void 회원가입이_정상적으로_동작하는_경우() {
        String userName = "userName";
        String password = "password";

        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userEntityRepository.save(any())).thenReturn((UserEntityFixture.get(userName,password,1)));

        assertDoesNotThrow(() -> userService.join(userName, password));
    }

    @Test
    void 회원가입시_userName으로_회원가입한_유저가_있는경우() {
        String userName = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName,password,1);

        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userEntityRepository.save(any())).thenReturn(Optional.of(fixture));

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> userService.join(userName, password));
        Assertions.assertEquals(ErrorCode.DUPLICATED_USER_NAME, e.getErrorCode());
    }

    @Test
    void 로그인이_정상적으로_동작하는_경우() {
        String userName = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName,password,1);

        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
        when(encoder.matches(password, fixture.getPassword())).thenReturn(true);
        assertDoesNotThrow(() -> userService.login(userName, password));
    }

    @Test
    void 로그인시_userName으로_회원가입한_유저가_없는경우() {
        String userName = "userName";
        String password = "password";

        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> userService.login(userName, password));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 로그인시_패스워드가_틀린경우() {
        String userName = "userName";
        String password = "password";
        String wrongPassword = "wrongPassword";

        UserEntity fixture = UserEntityFixture.get(userName,password,1);

        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> userService.login(userName, wrongPassword));
        Assertions.assertEquals(ErrorCode.INVALID_PASSWORD, e.getErrorCode());
    }


}
