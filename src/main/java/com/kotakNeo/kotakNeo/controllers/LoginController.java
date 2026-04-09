package com.kotakNeo.kotakNeo.controllers;

import com.kotakNeo.kotakNeo.Services.StockFetching;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.Map;

@RestController
//@RequestMapping("/login")
public class LoginController {
    private static String allResults = null;
    @Autowired
    private StockFetching stockFetching;
    @Autowired
    private RestTemplate restTemplate;
    @GetMapping
    public String welcome() {
        return "hi";
    }

    @GetMapping("/login/{accessToken}")
    public void step1(@PathVariable String accessToken, HttpServletResponse httpServletResponse) throws IOException {
        String url1 = "https://mis.kotaksecurities.com/login/1.0/tradeApiLogin";
        String baseUrl = null;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "78c450a5-ff8a-4f98-9077-680a8c0968ac");
        headers.set("neo-fin-key", "neotradeapi");
        headers.set("Content-Type", "application/json");
        String requestBody = """
                {
                "mobileNumber": "+918778199185",
                        "ucc": "V35FU",
                        "totp": "%s"
                }
                """.formatted(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url1, HttpMethod.POST,
                entity, Map.class);
        Map<String, Object> responseBody = response.getBody();
        if (responseBody.get("data") instanceof Map) {
            Map<?, ?> data = (Map<?, ?>) responseBody.get("data");
            System.out.println(data.get("token"));
            System.out.println(data.get("sid"));
            String url2 = "https://mis.kotaksecurities.com/login/1.0/tradeApiValidate";

            headers.set("sid", data.get("sid").toString());
            headers.set("Auth", data.get("token").toString());
            String mpin = "538294";
            requestBody = """
                    {
                            "mpin": "%s"
                    }
                    """.formatted(mpin);
            entity = new HttpEntity<>(requestBody, headers);
            response = restTemplate.exchange(url2, HttpMethod.POST,
                    entity, Map.class);
            responseBody = response.getBody();
            if (responseBody.get("data") instanceof Map) {
                data = (Map<?, ?>) responseBody.get("data");
                baseUrl = data.get("baseUrl").toString();
                System.out.println(baseUrl);

            }

        }
        httpServletResponse.sendRedirect("http://localhost:8081/mylist?baseUrl=" + baseUrl);
    }

    @GetMapping("/mylist")
    public void mylist(@RequestParam(required = false) String baseUrl, HttpServletResponse httpServletResponse) throws IOException {
           if(baseUrl == null|| baseUrl.isBlank() || baseUrl.isEmpty())
               baseUrl = "https://e22.kotaksecurities.com";
            stockFetching.callFetchStock(baseUrl);
            httpServletResponse.sendRedirect("http://localhost:8081/list");
        }


    @GetMapping("/list")
    public String list(){
        allResults = "<pre>" + stockFetching.getResult()+ "</pre>";
        return allResults;
    }

}
