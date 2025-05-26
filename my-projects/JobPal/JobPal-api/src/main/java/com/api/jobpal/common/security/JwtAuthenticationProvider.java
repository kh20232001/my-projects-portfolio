package com.api.jobpal.common.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.api.domain.models.dbdata.UserData;
import com.api.domain.repositories.UserRepository;

/**
 * ユーザIDとパスワードから認証を行う{@link AuthenticationProvider}の実装クラス
 */
@Component
class JwtAuthenticationProvider implements AuthenticationProvider {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userId = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        System.out.println("userId: " + userId + ", password: " + password);

        UserData userData = userRepository.selectTeacherStudentOne(userId, true);
        if (userData == null) {
            throw new AuthenticationServiceException("ユーザIDが見つかりません");
        }
        String encryptedPassword = userData.getPassword();
        String Grant = userData.getUserType();
        String status = userData.getUserStatus();

        if (!status.equals("0")) {
            throw new AuthenticationServiceException("ステータス無効");
        }
        if (!passwordEncoder.matches(password, encryptedPassword)) {
            throw new BadCredentialsException("パスワード不一致");
        }

        Collection<GrantedAuthority> GrantSet = Collections.singleton(new SimpleGrantedAuthority(Grant));
        UserDetails user = new User(userId, password, GrantSet);
        return new UsernamePasswordAuthenticationToken(user, null, GrantSet);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    // constructor
    public JwtAuthenticationProvider(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

}
