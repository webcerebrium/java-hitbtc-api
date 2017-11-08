package com.webcerebrium.hitbtc.api;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import com.webcerebrium.hitbtc.datatype.HitbtcTradingInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Data
@Slf4j
public class HitbtcApi {

    /* Actual API key and Secret Key that will be used */
    public String apiKey;
    public String secretKey;

    public HitbtcConfig config = new HitbtcConfig();

    /**
     * API Base URL
     */
    public String baseUrl = "https://api.hitbtc.com/api/";

    /**
     * Guava Class Instance for escaping
     */
    private Escaper esc = UrlEscapers.urlFormParameterEscaper();


    /**
     * Constructor of API when you exactly know the keys
     * @param apiKey Public API Key
     * @param secretKey Secret API Key
     * @throws HitbtcApiException in case of any error
     */
    public HitbtcApi(String apiKey, String secretKey) throws HitbtcApiException {

        this.apiKey = apiKey;
        this.secretKey = secretKey;
        validateCredentials();
    }

    /**
     * Constructor of API - keys are loaded from VM options, environment variables, resource files
     * @throws HitbtcApiException in case of any error
     */
    public HitbtcApi() {
        this.apiKey = config.getVariable("HITBTC_API_KEY");
        this.secretKey = config.getVariable("HITBTC_SECRET_KEY");
    }

    /**
     * Validation we have API keys set up
     * @throws HitbtcApiException in case of any error
     */
    protected void validateCredentials() throws HitbtcApiException {
        String humanMessage = "Please check environment variables or VM options";
        if (Strings.isNullOrEmpty(this.getApiKey()))
            throw new HitbtcApiException("Missing HITBTC_API_KEY. " + humanMessage);
        if (Strings.isNullOrEmpty(this.getSecretKey()))
            throw new HitbtcApiException("Missing HITBTC_SECRET_KEY. " + humanMessage);
    }

    // ======= ======= ======= ======= ======= =======
    // MARKET INFORMATION
    // ======= ======= ======= ======= ======= =======

    public HitbtcTradingInfo getSymbols() throws HitbtcApiException {
        return new HitbtcTradingInfo(new HitbtcRequest(baseUrl + "2/public/symbol").read().asJsonArray());
    }


    public Set<String> getCoinsOf(String coin) {
        try {
            HitbtcTradingInfo stats = getSymbols();
            return stats.getCoinsOf(coin.toUpperCase());
        } catch (Exception e) {
            log.error("Hitbtc UNCAUGHT EXCEPTION {}", e);
        } catch (HitbtcApiException e) {
            log.warn("Hitbtc ERROR {}", e.getMessage());
        }
        return ImmutableSet.of();
    }

}
