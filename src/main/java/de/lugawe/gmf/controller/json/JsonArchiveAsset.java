package de.lugawe.gmf.controller.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonArchiveAsset {

    @JsonProperty("archive_asset_path")
    private String path;

    public JsonArchiveAsset() {}

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
