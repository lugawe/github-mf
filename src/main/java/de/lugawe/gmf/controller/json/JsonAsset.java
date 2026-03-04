package de.lugawe.gmf.controller.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonAsset {

    @JsonProperty("asset_name")
    private String name;

    @JsonProperty("url")
    private String url;

    @JsonProperty("content_type")
    private String contentType;

    public JsonAsset() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
