package com.kotakNeo.kotakNeo.Services;

import com.kotakNeo.kotakNeo.entities.DataPackOne;
import com.kotakNeo.kotakNeo.entities.GapWithPredictionAndActualPrice;
import com.kotakNeo.kotakNeo.model.PredictionResponse;
import com.kotakNeo.kotakNeo.model.StockQuote;
import com.kotakNeo.kotakNeo.repositories.DataPackOneRepository;
import com.kotakNeo.kotakNeo.repositories.GapWithPredictionAndActualPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

@Service
public class StockFetching {
    @Autowired
    private DataPackOneRepository dataPackOneRepository;
    @Autowired
    private GapWithPredictionAndActualPriceRepository gapWithPredictionAndActualPriceRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private static StringBuilder sb = new StringBuilder();
    private static String baseUrl; // dynamic, set externally

    // Call this from your controller or service to set baseUrl dynamically
    public void callFetchStock(String url) {
        baseUrl = url;   // set the dynamic base URL
        fetch();         // trigger immediately

    }
    public String getResult(){
        return sb.toString();
    }
    // Scheduled job runs every second, no arguments allowed
    @Scheduled(fixedRate = 15*60*1000)
    public void fetch() {
        if (baseUrl == null || baseUrl.isBlank()) {
            return; // nothing to do until baseUrl is set
        }

        String url3 = baseUrl + "/script-details/1.0/quotes/neosymbol/"
                + "nse_cm|1594,bse_cm|500209,"
                + "nse_cm|11536,bse_cm|532540,"
                + "nse_cm|3456,bse_cm|500570,"
                + "nse_cm|759782,bse_cm|544569,"
                + "nse_cm|881,bse_cm|500124,"
                + "nse_cm|694,bse_cm|500087,"
                + "nse_cm|5948,bse_cm|532218,"
                + "nse_cm|3787,bse_cm|507685,"
                + "nse_cm|8054,bse_cm|532652,"
                + "nse_cm|10945,bse_cm|543596,"
                + "nse_cm|5192,bse_cm|523323,"
                + "nse_cm|1660,bse_cm|500875,"
                + "nse_cm|3918,bse_cm|524816/all";
        HttpHeaders newHeader = new HttpHeaders();
       newHeader.set("Authorization", "78c450a5-ff8a-4f98-9077-680a8c0968ac");
       HttpEntity<String> entity = new HttpEntity<>(newHeader);

        ResponseEntity<StockQuote[]> response =
                restTemplate.exchange(url3, HttpMethod.GET, entity, StockQuote[].class);
        LocalTime marketClose = LocalTime.of(15, 30);
        LocalTime marketStart = LocalTime.of(9, 15);
        sb.setLength(0); // clear previous results
        for (StockQuote quote : response.getBody()) {
//            if(quote.getDisplaySymbol().toString().equals("SOUTHBANK") && LocalTime.now().isBefore(marketClose)
//                && LocalTime.now().isAfter(marketStart)){
            if(LocalTime.now().isBefore(marketClose)
                    && LocalTime.now().isAfter(marketStart)){
                System.out.println("print DB "+ quote.getDisplaySymbol());
                DataPackOne dataPackOne = new DataPackOne();
                dataPackOne.setCreatedDate(Date.valueOf(LocalDate.now()));
                dataPackOne.setCreatedTime(Time.valueOf(LocalTime.now()));
                dataPackOne.setTotalBuyQuantity(quote.getTotalBuy().toString());
                dataPackOne.setTotalSellQuantity(quote.getTotalSell().toString());
                dataPackOne.setDifferences(Double.valueOf(quote.getTotalBuy()) -
                        Double.valueOf(quote.getTotalSell()));
                dataPackOne.setPrice(quote.getLtp());
               dataPackOne.setStockName(quote.getDisplaySymbol().toString());
               dataPackOneRepository.save(dataPackOne);
            }
            if(Integer.valueOf(quote.getTotalSell())> 2*Integer.valueOf(quote.getTotalBuy())){
                sb.append(quote.getDisplaySymbol()).append("  need to sell");
            } else if (2*Integer.valueOf(quote.getTotalSell()) < Integer.valueOf(quote.getTotalBuy())) {
                sb.append(quote.getDisplaySymbol()).append("  need to buy");
            }

            RestTemplate restTemplate = new RestTemplate();

            // Example path variables
            String stockName = quote.getDisplaySymbol().toString();
            double differences = (Double.parseDouble(quote.getTotalBuy()) -
                    Double.parseDouble(quote.getTotalSell()));
            int buyQty = Integer.parseInt(quote.getTotalBuy());
            int sellQty = Integer.parseInt(quote.getTotalSell());

            // Build URL with path variables
            String url = String.format(
                    "http://localhost:5000/predict/%s/%.2f/%d/%d",
                    stockName, differences, buyQty, sellQty
            );


            PredictionResponse predictionResponse =
                    restTemplate.getForObject(url, PredictionResponse.class);
            GapWithPredictionAndActualPrice gapWithPredictionAndActualPrice = new GapWithPredictionAndActualPrice();
            gapWithPredictionAndActualPrice.setPrice(quote.getLtp());
            gapWithPredictionAndActualPrice.setPredicted_price(predictionResponse.getPredicted_price());
            gapWithPredictionAndActualPrice.setStock_name(quote.getDisplaySymbol());
            gapWithPredictionAndActualPrice.setCreatedDate(Date.valueOf(LocalDate.now()));
            gapWithPredictionAndActualPrice.setCreatedTime(Time.valueOf(LocalTime.now()));
            gapWithPredictionAndActualPriceRepository.save(gapWithPredictionAndActualPrice);
            assert predictionResponse != null;
            sb.append(quote.getDisplaySymbol()).append("  ")
                    .append(quote.getLtp()).append("  ")
                    .append(predictionResponse.getPredicted_price()).append("  ")
                    .append(quote.getTotalBuy()).append("  ")
                    .append(quote.getTotalSell()).append("  ")
                    .append(quote.getOhlc().getLow()).append("  ")
                    .append(quote.getOhlc().getHigh())
                    .append("<br>")
                    .append("\n")
                    .append("\n");
        }
    }
}

