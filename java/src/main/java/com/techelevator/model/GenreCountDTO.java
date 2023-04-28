package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenreCountDTO {

    @JsonProperty("genre_name")
    private String name;
    @JsonProperty("count")
    private int count;

    public GenreCountDTO(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
