package de.lugawe.gmf.controller.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonRelease {

    @JsonProperty("release_name")
    private String name;

    @JsonProperty("assets")
    private List<JsonAsset> assets;

    public JsonRelease() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JsonAsset> getAssets() {
        return assets;
    }

    public void setAssets(List<JsonAsset> assets) {
        this.assets = assets;
    }
}
