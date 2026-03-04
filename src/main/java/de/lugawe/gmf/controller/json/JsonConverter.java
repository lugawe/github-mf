package de.lugawe.gmf.controller.json;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;

import de.lugawe.gmf.core.domain.ArchiveAsset;
import de.lugawe.gmf.core.domain.Asset;
import de.lugawe.gmf.core.domain.Release;

@ApplicationScoped
public class JsonConverter {

    public JsonConverter() {}

    public JsonArchiveAsset toJsonArchiveAsset(ArchiveAsset archiveAsset) {

        JsonArchiveAsset result = new JsonArchiveAsset();
        result.setPath(archiveAsset.getPath());

        return result;
    }

    public JsonAsset toJsonAsset(Asset asset) {

        JsonAsset result = new JsonAsset();
        result.setName(asset.getName());
        result.setUrl(asset.getUrl().toString());
        result.setContentType(asset.getContentType());

        return result;
    }

    public JsonRelease toJsonRelease(Release release) {

        JsonRelease result = new JsonRelease();
        result.setName(release.getName());

        List<Asset> assets = release.getAssets();
        if (assets != null) {
            result.setAssets(assets.stream().map(this::toJsonAsset).toList());
        }

        return result;
    }
}
