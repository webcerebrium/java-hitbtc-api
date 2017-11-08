package com.webcerebrium.hitbtc.datatype;

import com.google.common.base.Strings;
import com.webcerebrium.hitbtc.api.HitbtcApiException;


public class HitbtcSymbol {

    String symbol = "";

    public HitbtcSymbol(String symbol)  throws HitbtcApiException {
        // sanitizing symbol, preventing from common user-input errors
        if (Strings.isNullOrEmpty(symbol)) {
            throw new HitbtcApiException("Symbol cannot be empty. Example: ATLBTC");
        }
        if (symbol.contains(" ")) {
            throw new HitbtcApiException("Symbol cannot contain spaces. Example: ATLBTC");
        }
        if (!symbol.endsWith("BTC") && !symbol.endsWith("ETH")&& !symbol.endsWith("USD")) {
            throw new HitbtcApiException("Market Symbol should be ending with BTC, ETH or USD. Example: ATLBTC. Provided: " + symbol);
        }
        this.symbol = symbol.replace("_", "").replace("-", "").toUpperCase();
    }

    public String get(){ return this.symbol; }

    public String getSymbol(){ return this.symbol; }

    public String toString() { return this.get(); }

    public static HitbtcSymbol valueOf(String s) throws HitbtcApiException {
        return new HitbtcSymbol(s);
    }

    public static HitbtcSymbol BTC(String pair) throws HitbtcApiException {
        return HitbtcSymbol.valueOf(pair.toUpperCase() + "BTC");
    }

    public static HitbtcSymbol ETH(String pair) throws HitbtcApiException {
        return HitbtcSymbol.valueOf(pair.toUpperCase() + "ETH");
    }

    public static HitbtcSymbol USD(String pair) throws HitbtcApiException {
        return HitbtcSymbol.valueOf(pair.toUpperCase() + "USD");
    }

    public boolean contains(String coin) {
        return (symbol.endsWith(coin.toUpperCase())) || (symbol.startsWith(coin.toUpperCase()));
    }

    public String getOpposite(String coin) {
        if (symbol.startsWith(coin)) {
            return symbol.substring((coin).length());
        }
        if (symbol.endsWith(coin)) {
            int index = symbol.length() - (coin).length();
            return symbol.substring(0, index);
        }
        return "";
    }

}
