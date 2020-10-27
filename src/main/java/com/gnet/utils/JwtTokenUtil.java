package com.gnet.utils;


/*import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;*/

import com.jfinal.log.Logger;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Slf4j
public class JwtTokenUtil {

    //公用密钥
    public static String SECRET = "zheJingJinFangSchool";

    //生成Token
    public static String createToken(String userId,String role) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("role",role);
        //签发时间
        Date iatDate = new Date();
        //过期时间  120分钟后过期
        long now = System.currentTimeMillis();
        //6个小时过期
        long exp = now + 1000*60*60*6;
        //Date expiresDate = nowTime.getTime();
        JwtBuilder builder =Jwts.builder()
                .signWith(SignatureAlgorithm.HS512,SECRET)
                .setClaims(claims)
                .setIssuer("Jinfang")
                .setSubject(userId)
                .setIssuedAt(iatDate)
                .setExpiration(new Date(exp));
        String token=builder.compact();


        return token;
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public static Boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("zheJingJinFangSchool")
                    .parseClaimsJws(token).getBody();
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy‐MM‐dd hh:mm:ss");
           /* System.out.println("签发时间:"+sdf.format(claims.getIssuedAt()));
            System.out.println("过期时间:"+sdf.format(claims.getExpiration()));
            System.out.println("当前时间:"+sdf.format(new Date()) );*/
            Date expiration = claims.getExpiration();
            boolean before = expiration.before(new Date());
            return expiration.before(new Date());
        } catch (Exception e) {
            log.error("Can't find token from Authorization|token..");
            return true;
        }
    }


}

