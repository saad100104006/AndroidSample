package uk.co.transferx.app.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sergey on 05/04/2018.
 */

public class Rates {

    @SerializedName("rates")
    @Expose
    private List<Rate> rates;

    public List<Rate> getRates() {
        return rates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rates rates1 = (Rates) o;

        return rates != null ? rates.equals(rates1.rates) : rates1.rates == null;
    }

    @Override
    public int hashCode() {
        return rates != null ? rates.hashCode() : 0;
    }
}
