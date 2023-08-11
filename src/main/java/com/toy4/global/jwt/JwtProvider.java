package com.toy4.global.jwt;

import com.toy4.domain.employee.type.EmployeeRole;
import com.toy4.global.security.CustomUserDetailsService;
import com.toy4.global.token.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String KEY_ROLE = "role";

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 60 * 60 * 1000L; // 1 hour
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 14 * 24 * 60 * 60 * 1000L; // 14 day

    private final CustomUserDetailsService customUserDetailsService;
    private final Key secretKey;

    public JwtProvider(CustomUserDetailsService customUserDetailsService, KeyMaker keyMaker) {
        this.customUserDetailsService = customUserDetailsService;
        this.secretKey = keyMaker.getKey();
    }

    /** 토큰 생성 */
    public TokenDto generateToken(String email, EmployeeRole role) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put(KEY_ROLE, role);

        Date now = new Date();

        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(secretKey)
                .compact();

        return TokenDto.builder()
                .grantType(TOKEN_PREFIX.substring(0, TOKEN_PREFIX.length() - 1))
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpireDate(ACCESS_TOKEN_EXPIRE_TIME)
                .build();
    }

    /** 토큰 추출 */
    public String resolveTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);

        if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());
        }

        return null;
    }

    /** 토큰 유효성 검사 */
    public boolean validateToken(String token) {
        // token 값이 빈 값일 경우 유효하지 않다.
        if (!StringUtils.hasText(token)) {
            return false;
        }

        // 토큰 만료시간이 현재 시간보다 이전인지 아닌지 확인
        Claims claims = parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    /** Spring Security 인증 과정에서 권한 확인 */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(this.getSubjectByAccessToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /** SUBJECT(이메일) 가져오기 */
    public String getSubjectByAccessToken(String accessToken) {
        return parseClaims(accessToken).getSubject();
    }

    /** JWT 토큰 복호화 후 가져오기 */
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
//            throw new RuntimeException("토큰이 만료되었습니다.");
            return e.getClaims();
        }
    }

}
