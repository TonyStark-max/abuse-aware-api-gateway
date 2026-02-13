package com.api_gateway.Components;

import org.springframework.stereotype.Component;

@Component
public class TokenBucket {
    private final int capacity;
    private final int refillRatePerSecond;

    private int tokens;
    private long lastRefillTimeStamp;

    public TokenBucket(int capacity,int refillRatePerSecond){
        this.capacity=capacity;
        this.refillRatePerSecond=refillRatePerSecond;
        this.tokens=capacity;
        this.lastRefillTimeStamp=System.currentTimeMillis();
    }

    public synchronized boolean tryConsume(){
        refill();

        if(tokens>0){
            tokens--;
            return true;
        }
        return false;
    }

    private void refill(){
        long now=System.currentTimeMillis();
        long elapsedMillis=now-lastRefillTimeStamp;

        long tokensToAdd=(elapsedMillis/1000)*refillRatePerSecond;

        if(tokensToAdd>0){
            tokens=Math.min(capacity,tokens+(int) tokensToAdd);
            lastRefillTimeStamp=now;
        }
    }
}