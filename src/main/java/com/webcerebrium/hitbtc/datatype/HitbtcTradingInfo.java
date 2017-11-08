package com.webcerebrium.hitbtc.datatype;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.webcerebrium.hitbtc.api.HitbtcApiException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


@Slf4j
@Data
public class HitbtcTradingInfo {

    List<HitbtcTradingSymbol> symbols = new LinkedList<>();

    public HitbtcTradingInfo() {
    }

    public HitbtcTradingInfo(JsonArray response) throws HitbtcApiException {
        symbols.clear();
        for (JsonElement element: response) {
            HitbtcTradingSymbol symbol = new HitbtcTradingSymbol(element.getAsJsonObject());
            symbols.add(symbol);
        }
    }

    public List<HitbtcTradingSymbol> getMarketsOf(String coin) {
        List<HitbtcTradingSymbol> result = new LinkedList<>();
        for (int i = 0; i < symbols.size(); i++ ) {
            HitbtcTradingSymbol tradingSymbol = symbols.get(i);
            if (tradingSymbol.getBaseCurrency().equals(coin) || tradingSymbol.getQuoteCurrency().equals(coin)) {
                result.add(tradingSymbol);
            }
        }
        return result;
    }

    public Set<HitbtcSymbol> getSymbolsOf(String coin) throws HitbtcApiException {
        List<HitbtcTradingSymbol> coins = getMarketsOf(coin);
        Set<HitbtcSymbol> result = new TreeSet<>();
        for (HitbtcTradingSymbol sym: coins) {
            result.add(sym.getSymbol());
        }
        return result;
    }

    public Set<String> getCoinsOf(String coin) throws HitbtcApiException {
        List<HitbtcTradingSymbol> coins = getMarketsOf(coin);
        Set<String> result = new TreeSet<>();
        for (HitbtcTradingSymbol sym: coins) {
            result.add(sym.getSymbol().getOpposite(coin));
        }
        return result;
    }

}
