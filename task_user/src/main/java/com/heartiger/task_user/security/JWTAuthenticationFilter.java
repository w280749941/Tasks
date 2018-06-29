//package com.heartiger.task_user.security;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.Gson;
//import com.heartiger.task_user.datamodel.UserInfo;
//import com.heartiger.task_user.dto.JwtToken;
//import com.heartiger.task_user.form.UserForm;
//import com.heartiger.task_user.utils.ResultDTOUtil;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import static com.heartiger.task_user.security.SecurityConstants.*;
//
//public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//    private AuthenticationManager authenticationManager;
//
//    JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//
//        UserInfo userInfo = new UserInfo();
//        userInfo.setEmail(request.getParameter("username"));
//        userInfo.setPasscode(request.getParameter("password"));
//
//        return authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        userInfo.getEmail(),
//                        userInfo.getPasscode(),
//                        new ArrayList<>()
//                )
//        );
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//
//        Date expiredTime = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
//        String token = Jwts.builder()
//                .setSubject(((User) authResult.getPrincipal()).getUsername())
//                .setExpiration(expiredTime)
//                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
//                .compact();
//
//        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
//        response.setContentType("application/json");
//        Gson gson = new Gson();
//        JwtToken jwtToken = new JwtToken(token, expiredTime.getTime());
//        response.getWriter().print(gson.toJson(ResultDTOUtil.success(jwtToken)));
//        response.getWriter().flush();
//    }
//}
