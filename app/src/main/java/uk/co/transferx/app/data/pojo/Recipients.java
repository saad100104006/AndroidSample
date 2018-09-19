package uk.co.transferx.app.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

/**
 * Created by sergey on 03/04/2018.
 */

public class Recipients {

    public List<Recipient> getResipients() {
        return resipients == null ? Collections.emptyList() : resipients;
    }

    @SerializedName("recipients")
    @Expose
    private List<Recipient> resipients;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipients that = (Recipients) o;

        return resipients != null ? resipients.equals(that.resipients) : that.resipients == null;
    }

    @Override
    public int hashCode() {
        return resipients != null ? resipients.hashCode() : 0;
    }
}
