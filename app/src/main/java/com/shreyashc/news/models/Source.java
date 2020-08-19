package com.shreyashc.news.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Source {
    @SerializedName("id")
    @Expose
    private String id;
    @Expose
    @SerializedName("name")
    private String name;

    public Source(String id, String name) {
        this.id = id;
        this.name = name;
    }

// Getter Methods

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Setter Methods

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}