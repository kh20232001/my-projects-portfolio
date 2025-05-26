package com.api.jobpal.common.security;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.api.domain.repositories.UserRepository;
import com.api.domain.validations.UserAuthority;
import com.api.jobpal.common.base.ResponseMessage;
import com.api.jobpal.common.base.WebConfig;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * セキュリティ設定クラス.
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    /**
     * [propertiesで設定]動作環境(h2-consoleの使用許可)
     */
    @Value("${settings.profiles.active:local}")
    private String PROFILE_ACTIVE;

    /**
     * [propertiesで設定]JWTの作成に使用する秘密鍵
     */
    @Value("${security.secret-key:secret}")
    private String SECRET_KEY;

    /**
     * [propertiesで設定]アクセスを許可するフロントアプリのURL
     */
    @Value("${allowed.origins:http://localhost:5555}")
    private String ALLOWED_ORIGINS;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Bean
    @Autowired
    SecurityFilterChain securityFilterChain(HttpSecurity http, Map<String, JWTVerifier> jwtVerifierMap)
            throws Exception {

        if ("local".equals(PROFILE_ACTIVE)) {
            // テスト環境の場合はh2console, swagger UI, dummyエンドポイントを許可
            http.authorizeHttpRequests(authz -> {
                authz.requestMatchers(
                        AntPathRequestMatcher.antMatcher("/h2-console/**"),
                        AntPathRequestMatcher.antMatcher("/api-docs/**"),
                        AntPathRequestMatcher.antMatcher("/swagger-ui/**"),
                        AntPathRequestMatcher.antMatcher("/swagger-resources/**"),
                        AntPathRequestMatcher.antMatcher("/swagger-ui.html"),
                        AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/dummy/**"))
                        .permitAll();
            });
            http.headers((headers) -> headers.disable());
        }

        http.authorizeHttpRequests(authz -> {
            authz.requestMatchers(
                    AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/login"))
                    .permitAll() // ログイン不要

                    .requestMatchers(
                            AntPathRequestMatcher.antMatcher("/new/**"))
                    .hasAuthority(UserAuthority.STUDENT.getGrantId()) // 学生用

                    .requestMatchers(
                            AntPathRequestMatcher.antMatcher("/home/**"),
                            AntPathRequestMatcher.antMatcher("/homedetail/**"))
                    .hasAnyAuthority(UserAuthority.ADMIN.getGrantId(), UserAuthority.TEACHER.getGrantId(),
                            UserAuthority.STUDENT.getGrantId()) // 管理者、担任、学生用

                    .requestMatchers(
                            AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/user/**"))
                    .hasAnyAuthority(UserAuthority.ADMIN.getGrantId(), UserAuthority.TEACHER.getGrantId()) // 管理者と教師用

                    .requestMatchers(
                            AntPathRequestMatcher.antMatcher(HttpMethod.PUT, "/user/**"),
                            AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, "/user/**"),
                            AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/user/**"))
                    .hasAuthority(UserAuthority.ADMIN.getGrantId()) // 管理者のみ

                    // 上記以外はログインを必須とする
                    .anyRequest().authenticated();
        })

                // Exception
                .exceptionHandling(exc -> {
                    exc.authenticationEntryPoint((request, response, exception) -> {
                        if (response.isCommitted())
                            return;
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write(objectMapper.writeValueAsString(ResponseMessage.NO_LOGIN));
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    });
                    // PUTリクエストに対してのみ400エラーを返す
                    exc.accessDeniedHandler((request, response, accessDeniedException) -> {
                        SecurityContextHolder.clearContext();
                        response.setCharacterEncoding("UTF-8");
                        if ("PUT".equalsIgnoreCase(request.getMethod())) {
                            // PUTの場合は400 Bad Request
                            response.getWriter().write(objectMapper.writeValueAsString("PUTリクエストに対する権限エラーです"));
                            response.setStatus(HttpStatus.BAD_REQUEST.value());
                        } else if ("DELETE".equalsIgnoreCase(request.getMethod())) {
                            // DELETEの場合は403 Bad Request
                            response.getWriter().write(objectMapper.writeValueAsString("DELETEリクエストに対する権限エラーです"));
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                        } else {
                            // その他のリクエストには499 Forbidden
                            response.getWriter()
                                    .write(objectMapper.writeValueAsString(ResponseMessage.FORBIDDEN_MESSAGE));
                            response.setStatus(499);
                        }
                    });
                })

                // logout
                .logout(logout -> {
                    logout.logoutUrl("/logout")
                            // ログアウト時は200ステータスを返却するように設定
                            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
                })

                // AUTHORIZE
                .addFilterBefore(new JwtTokenFilter(jwtVerifierMap.get("accessVerifier"), userRepository, objectMapper),
                        UsernamePasswordAuthenticationFilter.class)

                // SESSION
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // CORS
                .cors(cors -> cors.configurationSource(this.corsFilter()))

                // CSRF
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    /**
     * CORS設定を構築する.
     *
     * @return source
     */
    private CorsConfigurationSource corsFilter() {
        // CORS設定の作成
        CorsConfiguration configuration = new CorsConfiguration();

        // Cookie情報の取得許可
        configuration.setAllowCredentials(true);

        // 許可ドメイン
        configuration.setAllowedOrigins(Collections.singletonList(ALLOWED_ORIGINS));
        configuration.setAllowedOriginPatterns(Collections.singletonList(ALLOWED_ORIGINS));

        // 許可ヘッダー情報
        configuration.setAllowedHeaders(List.of(
                "Access-Control-Allow-Headers",
                "Access-Control-Allow-Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers",
                "Access-Control-Expose-Headers",
                "Cache-Control", "Content-Type",
                "Accept-Language",
                WebConfig.AUTHORIZATION_HEADER_NAME));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setExposedHeaders(List.of(WebConfig.AUTHORIZATION_HEADER_NAME));

        // 許可URL
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    /**
     * リクエストボディからログイン処理を行うためのフィルタ生成.
     *
     * @param authenticationManager フィルターに渡すマネージャー
     * @return フィルタ
     */
    @Bean
    @Autowired
    UsernamePasswordAuthenticationFilter jwtUsernamePasswordAuthenticationFilter(
            AuthenticationManager authenticationManager, Algorithm algorithm) {
        JwtUsernamePasswordAuthenticationFilter filter = new JwtUsernamePasswordAuthenticationFilter(
                authenticationManager, objectMapper);
        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
        filter.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler(algorithm, userRepository));
        filter.setAuthenticationFailureHandler(new JwtAuthenticationFailureHandler(objectMapper));
        return filter;
    }

    /**
     * JWTの作成に使用するアルゴリズム設定.
     *
     * @return アルゴリズムオブジェクト
     */
    @Bean
    Algorithm algorithm() {
        return Algorithm.HMAC512(SECRET_KEY);
    }

    /**
     * アクセストークンの検証に使用するオブジェクト.
     *
     * @param algorithm トークン作成時に使用したアルゴリズムオブジェクト
     * @return アクセストークンの検証に使用するオブジェクト
     */
    @Bean
    @Autowired
    JWTVerifier accessVerifier(Algorithm algorithm) {
        return JWT.require(algorithm).build();
    }

    /**
     * Authenticationリクエストを処理するオブジェクト.
     *
     * @param authenticationConfiguration authenticationConfiguration
     * @return AuthenticationManager
     * @throws Exception 例外
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
