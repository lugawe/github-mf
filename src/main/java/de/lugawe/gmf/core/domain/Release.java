package de.lugawe.gmf.core.domain;

import java.util.List;

public final class Release {

    private String name;
    private List<Asset> assets;

    public Release() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }
}
