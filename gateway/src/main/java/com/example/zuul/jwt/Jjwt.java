package com.example.zuul.jwt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.zuul.dto.TokenDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gxkai on 2018-11-23 9:43 AM
 **/
public class Jjwt {
    private static String JWT_SECRET = "7786df7fc3a34e26a61c034d5ec8245d";
    private static Long ttlMillis = Long.valueOf(600000);

    public static String createJWT(String subject) throws Exception {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Map<String,Object> claims = new HashMap<>();
        SecretKey key = generalKey();
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setId("jwt")
                .setIssuedAt(now)
                .setSubject(subject)
                .signWith(signatureAlgorithm, key);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    public static Claims parseJWT(String jwt) {
        SecretKey key = generalKey();
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt).getBody();
        return claims;
    }

    public static SecretKey generalKey(){
        byte[] encodedKey = Base64.decodeBase64(JWT_SECRET);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    public static Boolean verifyJwt(String jwt) {
        if (StringUtils.isEmpty(jwt)) {
            return false;
        }
        Claims claims = parseJWT(jwt);
        JSONObject jsonObject = JSON.parseObject(claims.getSubject());
        TokenDTO tokenDTO = JSON.parseObject(jsonObject.toString(), TokenDTO.class);
        if (StringUtils.isEmpty(tokenDTO.getId())) {
            return false;
        }
        return true;
    }
}
