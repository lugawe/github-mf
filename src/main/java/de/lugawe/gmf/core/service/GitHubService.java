package de.lugawe.gmf.core.service;

import java.io.InputStream;
import java.util.List;

import de.lugawe.gmf.core.domain.ArchiveAsset;
import de.lugawe.gmf.core.domain.Asset;
import de.lugawe.gmf.core.domain.Release;

public interface GitHubService {

    Release getRelease(String repository, String tagName);

    List<Asset> getAssets(String repository, String tagName);

    Asset getAsset(String repository, String tagName, String assetName);

    InputStream getAssetContent(String repository, String tagName, String assetName);

    List<ArchiveAsset> getArchiveAssets(String repository, String tagName, String assetName);

    InputStream getArchiveAssetContent(String repository, String tagName, String assetName, String path);
}
