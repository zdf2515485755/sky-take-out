package com.sky.utils;

import com.sky.constant.JwtClaimsConstant;
import com.sky.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mrzhang
 */
public class JwtUtil {
    /**
     * 生成jwt
     * 使用Hs256算法, 私匙使用固定秘钥
     *
     */

    private JwtUtil() {
        throw new IllegalStateException("Utility class");
    }
    public static String createJWT(String secretKey, long ttlMillis, Long userId) {
        // 指定签名的时候使用的签名算法，也就是header那部分
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成JWT的时间
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, userId);

        // 设置jwt的body
        JwtBuilder builder = Jwts.builder()
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8))
                // 设置过期时间
                .setExpiration(exp);

        return builder.compact();
    }

    /**
     * Token解密
     *
     * @param secretKey jwt秘钥 此秘钥一定要保留好在服务端, 不能暴露出去, 否则sign就可以被伪造, 如果对接多个客户端建议改造成多个
     * @param token     加密后的token
     * @return
     */
    public static Claims parseJWT(String secretKey, String token) {
        // 得到DefaultJwtParser
        return Jwts.parser()
                // 设置签名的秘钥
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                // 设置需要解析的jwt
                .parseClaimsJws(token).getBody();

    }
    public static void main(String[] args) {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setAdminSecretKey("itcast");
        jwtProperties.setAdminTtl(7200000);

        String jwt = JwtUtil.createJWT(jwtProperties.getAdminSecretKey(), jwtProperties.getAdminTtl(), 1L);
        System.out.println("jwt: " + jwt);

        Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), jwt);
        Object o = claims.get(JwtClaimsConstant.EMP_ID);
        System.out.println(o);

    }

}
