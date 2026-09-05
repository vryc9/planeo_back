package com.example.planeo_back.infrastructure.adapter.investment.yahoo;

import com.example.planeo_back.infrastructure.adapter.investment.record.YahooResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface YahooFinanceClient {
    @GetExchange("/v8/finance/chart/{symbol}")
    YahooResponse getChart(@PathVariable String symbol,
                           @RequestParam(defaultValue = "1d") String range,
                           @RequestParam(defaultValue = "1m") String interval);
}
