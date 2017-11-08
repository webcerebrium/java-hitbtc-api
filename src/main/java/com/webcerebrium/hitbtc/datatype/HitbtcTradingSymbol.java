package com.webcerebrium.hitbtc.datatype;

import com.google.gson.JsonObject;
import com.webcerebrium.hitbtc.api.HitbtcApiException;
import com.webcerebrium.kucoin.api.KucoinApiException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Slf4j
public class HitbtcTradingSymbol {

    HitbtcSymbol symbol;
    String id;
    String baseCurrency;
    String quoteCurrency;
    BigDecimal quantityIncrement;
    BigDecimal tickSize;
    BigDecimal takeLiquidityRate;
    BigDecimal provideLiquidityRate;
    String feeCurrency;

    private void jsonExpect(JsonObject obj, Set<String> fields) throws KucoinApiException {
        Set<String> missing = new HashSet<>();
        for (String f: fields) { if (!obj.has(f) || obj.get(f).isJsonNull()) missing.add(f); }
        if (missing.size() > 0) {
            log.warn("Missing fields {} in {}", missing.toString(), obj.toString());
            throw new KucoinApiException("Missing fields " + missing.toString());
        }
    }

    private BigDecimal safeDecimal(JsonObject obj, String field) {
        if (obj.has(field) && obj.get(field).isJsonPrimitive() && obj.get(field) != null) {
            try {
                return obj.get(field).getAsBigDecimal();
            } catch (java.lang.NumberFormatException nfe) {
                log.info("Number format exception in field={} value={} trade={}", field, obj.get(field), obj.toString());
            }
        }
        return null;
    }

    public HitbtcTradingSymbol() {
    }

    public HitbtcTradingSymbol(JsonObject obj) throws HitbtcApiException {
        quantityIncrement = safeDecimal(obj, "quantityIncrement");
        tickSize = safeDecimal(obj, "tickSize");
        takeLiquidityRate = safeDecimal(obj, "takeLiquidityRate");
        provideLiquidityRate = safeDecimal(obj, "provideLiquidityRate");

        id = obj.get("id").getAsString();
        baseCurrency = obj.get("baseCurrency").getAsString();
        quoteCurrency = obj.get("quoteCurrency").getAsString();
        feeCurrency = obj.get("feeCurrency").getAsString();

        symbol = HitbtcSymbol.valueOf(id);
    }

}
