package com.api_gateway.Service;

import com.api_gateway.Components.TokenBucket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class RateLimiter {

    private final Map<String, TokenBucket> bucketStore=new ConcurrentHashMap<>();

    public boolean allowRequest(String apiKey){
        if(apiKey==null || apiKey.isBlank()){
            return false;
        }

        TokenBucket bucket=bucketStore.computeIfAbsent(
                apiKey,
                key->new TokenBucket(10,5)
        );

        return bucket.tryConsume();
    }
}
