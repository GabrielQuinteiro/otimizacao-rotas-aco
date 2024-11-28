package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {
    private String endereco;
    @JsonProperty("is_starting")
    private boolean isStarting;

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public boolean isStarting() {
        return isStarting;
    }

    public void setStarting(boolean isStarting) {
        this.isStarting = isStarting;
    }
}
