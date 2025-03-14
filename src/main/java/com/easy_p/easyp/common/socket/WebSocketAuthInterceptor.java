package com.easy_p.easyp.common.socket;

import com.easy_p.easyp.common.exception.JwtTokenException;
import com.easy_p.easyp.common.jwt.JwtProvider;
import com.easy_p.easyp.dto.MemberContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketAuthInterceptor implements HandshakeInterceptor {
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
        String token = Optional.ofNullable((req.getParameter("token"))).orElse("");
        if(token.isEmpty()){
            return false;
        }
        try{
            jwtProvider.validateToken(token);
            String sub = jwtProvider.getSub(token);
            if(!sub.equals("access-token")){
                return false;
            }
            String email = jwtProvider.getClaim(token, "email");
            MemberContext memberContext = (MemberContext) userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(memberContext, "", memberContext.getAuthorities());
            attributes.put("principal", authenticationToken);
            return true;
        }
        catch (JwtTokenException e){
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
