package com.heartiger.taskapigateway.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.heartiger.taskapigateway.exception.RateLimitException;
import com.netflix.zuul.ZuulFilter;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVLET_DETECTION_FILTER_ORDER;

@Component
public class FlowRateFilter extends ZuulFilter {

    private static final RateLimiter RATE_LIMITER = RateLimiter.create(100);

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return SERVLET_DETECTION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run(){
        // No ticket given
        if(!RATE_LIMITER.tryAcquire()){
            throw new RateLimitException();
        }
        return null;
    }
}
