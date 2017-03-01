package com.aweiz.alfreso.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by daweizhuang on 2/27/17.
 */
public class Ticket {
    @JsonProperty("entry")
    private TicketData data;

    public TicketData getData() {
        return data;
    }

    public void setData(TicketData data) {
        this.data = data;
    }
}
