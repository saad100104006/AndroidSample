package uk.co.transferx.app.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sergey on 05/04/2018.
 */

public class Rate {

    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("created")
    @Expose
    private long created;

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getRate() {
        return rate;
    }

    public long getCreated() {
        return created;
    }
}
