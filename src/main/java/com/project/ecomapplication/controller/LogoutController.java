package com.project.ecomapplication.controller;

import com.project.ecomapplication.services.AccessTokenService;
//import com.project.ecomapplication.services.BlackListTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

@RestController
public class LogoutController {

    @Autowired
    AccessTokenService accessTokenService;

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest request) {
        String token = accessTokenService.parseJwt(request);
        if (token == null) {
            return new ResponseEntity<>("Token not found", HttpStatus.BAD_REQUEST);
        }

        return accessTokenService.deleteToken(token);


    }
}
    /*@Transactional
    public String logout( HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            String tokenValue = bearerToken.substring(7, bearerToken.length());
            System.out.println(tokenValue);

            User user = accessTokenService.findUserByAcessToken(tokenValue);
            accessTokenService.deleteByUser(user);

            refreshTokenService.findUserByRefreshToken(tokenValue);
            refreshTokenService.deleteByUser(user);



           /* TokenDelete tokenDelete = new TokenDelete();
            Optional<RefreshToken> token=refreshTokenRepository.findByToken(refreshToken);
            tokenDelete.setToken(token.get().getToken());
            tokenDelete.setUser(token.get().getUser());
            tokenDeleteRepository.save(tokenDelete);
            refreshTokenRepository.deleteByToken(tokenValue);
        }*/

