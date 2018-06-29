package com.heartiger.taskapigateway.filter;

import com.google.gson.Gson;
import com.heartiger.taskapigateway.constant.RedisConstant;
import com.heartiger.taskapigateway.message.dto.JwtToken;
import com.netflix.discovery.util.StringUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

@Component
@Slf4j
public class TokenFilter extends ZuulFilter {

  private final StringRedisTemplate stringRedisTemplate;
  private final Gson gson;
  private static final String TOKEN_PREFIX = "Bearer ";

  @Autowired
  public TokenFilter(Gson gson, StringRedisTemplate stringRedisTemplate) {
    this.gson = gson;
    this.stringRedisTemplate = stringRedisTemplate;
  }

  @Override
  public String filterType() {
      return PRE_TYPE;
  }

  @Override
  public int filterOrder() {
      return PRE_DECORATION_FILTER_ORDER - 1;
  }

  @Override
  public boolean shouldFilter() {
    RequestContext requestContext = RequestContext.getCurrentContext();
    HttpServletRequest request = requestContext.getRequest();
    return notLoginNorRegister(request.getRequestURI());
  }

  private boolean notLoginNorRegister(String requestURI) {
    return !requestURI.equalsIgnoreCase("/user/api/users/login")
        && !requestURI.equalsIgnoreCase("/user/api/users/new");
  }

  @Override
  public Object run(){
    RequestContext requestContext = RequestContext.getCurrentContext();
    HttpServletRequest request = requestContext.getRequest();

    String tokenFromRequest = request.getHeader("Authorization");
    if(!StringUtils.isEmpty(tokenFromRequest)){
      String token = tokenFromRequest.replace(TOKEN_PREFIX, "");
      String tokenJson = stringRedisTemplate.opsForValue().get(String.format(RedisConstant.USER_TOKEN_TEMPLATE, token));
      if(tokenJson != null){
        JwtToken jwtToken = gson.fromJson(tokenJson, JwtToken.class);
        //TODO verify token not expired. Refresh token, invalid token...
        //TODO need to put id in header maybe, so that other services can use it to verify identity.

        log.info("Token from redis: {}", jwtToken);
        return null;
      }
    }

    requestContext.setSendZuulResponse(false);
    requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
    return null;
  }
}
