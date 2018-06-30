package com.heartiger.taskapigateway.filter;

import static com.heartiger.taskapigateway.constant.Constants.TOKEN_PREFIX;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

import com.google.gson.Gson;
import com.heartiger.taskapigateway.constant.Constants;
import com.heartiger.taskapigateway.dto.ResultDTO;
import com.heartiger.taskapigateway.enums.ResultEnum;
import com.heartiger.taskapigateway.message.dto.JwtToken;
import com.heartiger.taskapigateway.utils.ResultDTOUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TokenFilter extends ZuulFilter {

  private final StringRedisTemplate stringRedisTemplate;
  private final Gson gson;


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

  @Override
  public Object run(){
      RequestContext requestContext = RequestContext.getCurrentContext();
      HttpServletRequest request = requestContext.getRequest();

      String tokenFromRequest = request.getHeader(Constants.REQUEST_HEADER_TOKEN);
      if(!StringUtils.isEmpty(tokenFromRequest)){
          String token = tokenFromRequest.replace(TOKEN_PREFIX, "");
          String tokenJson = stringRedisTemplate.opsForValue().get(String.format(Constants.USER_TOKEN_TEMPLATE, token));
          if(tokenJson != null){
              String tokenFromRedis = gson.fromJson(tokenJson, String.class);
              if(request.getRequestURI().equals(Constants.LOGOUT_URI)) {
                  requestContext.setSendZuulResponse(false);
                  requestContext.addZuulResponseHeader("content-type", ContentType.APPLICATION_JSON.toString());
                  requestContext.setResponseBody(gson.toJson(LogOutUser(token)));
              }
              log.info("Token from redis: {}", tokenFromRedis);
              return null;
          }
      }

      requestContext.setSendZuulResponse(false);
      requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
      requestContext.addZuulResponseHeader("content-type", ContentType.APPLICATION_JSON.toString());
      requestContext.setResponseBody(gson.toJson(ResultDTOUtil.error(ResultEnum.UNAUTHORIZED_ACCESS)));
      return null;
  }

  private ResultDTO<Object> LogOutUser(String token) {
    stringRedisTemplate.delete(String.format(Constants.USER_TOKEN_TEMPLATE, token));
    log.info("User logout {}", token);
    return ResultDTOUtil.success();
  }

  private boolean notLoginNorRegister(String requestURI) {
    return !requestURI.equalsIgnoreCase("/user/api/users/login")
        && !requestURI.equalsIgnoreCase("/user/api/users/new");
  }
}
