package com.kotakNeo.kotakNeo.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class StockQuote {
    @JsonProperty("exchange_token")
    private String exchangeToken;

    @JsonProperty("display_symbol")
    private String displaySymbol;

    private String exchange;

    @JsonProperty("lstup_time")
    private String lstupTime;

    private String ltp;

    @JsonProperty("last_traded_quantity")
    private String lastTradedQuantity;

    @JsonProperty("total_buy")
    private String totalBuy;

    @JsonProperty("total_sell")
    private String totalSell;

    @JsonProperty("last_volume")
    private String lastVolume;

    @JsonProperty("avg_cost")
    private String avgCost;

    @JsonProperty("open_int")
    private String openInt;

    private String change;

    @JsonProperty("per_change")
    private String perChange;

    @JsonProperty("low_price_range")
    private String lowPriceRange;

    @JsonProperty("high_price_range")
    private String highPriceRange;

    @JsonProperty("year_high")
    private String yearHigh;

    @JsonProperty("year_low")
    private String yearLow;

    private Ohlc ohlc;
    private Depth depth;

    // Getters and Setters omitted for brevity
    @Getter
    public static class Ohlc {
        private String open;
        private String high;
        private String low;
        private String close;
        // Getters and Setters
    }

    public static class Depth {
        private List<QuoteEntry> buy;
        private List<QuoteEntry> sell;
        // Getters and Setters
    }

    public static class QuoteEntry {
        private String price;
        private String quantity;
        private String orders;
        // Getters and Setters
    }
}
