package de.lugawe.gmf.controller;

import java.io.InputStream;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import de.lugawe.gmf.controller.json.JsonArchiveAsset;
import de.lugawe.gmf.controller.json.JsonAsset;
import de.lugawe.gmf.controller.json.JsonConverter;
import de.lugawe.gmf.controller.json.JsonRelease;
import de.lugawe.gmf.core.domain.ArchiveAsset;
import de.lugawe.gmf.core.domain.Asset;
import de.lugawe.gmf.core.domain.Release;
import de.lugawe.gmf.core.exception.GMFException;
import de.lugawe.gmf.core.service.GitHubService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class RepoControllerService {

    private static final Logger log = LoggerFactory.getLogger(RepoControllerService.class);

    @Inject
    private JsonConverter jsonConverter;

    @Inject
    private GitHubService gitHubService;

    public RepoControllerService() {}

    private String resolveTagName(String tagName) {
        return (tagName == null || tagName.isEmpty()) ? "latest" : tagName;
    }

    public JsonRelease getRelease(String repository, String tagName) {

        tagName = resolveTagName(tagName);

        log.info("Getting release '{}' from repository '{}'.", tagName, repository);

        Release release;
        try {
            release = gitHubService.getRelease(repository, tagName);
        } catch (Exception e) {
            throw new GMFException("Could not get release '" + tagName + "' from '" + repository + "'.", e);
        }

        return jsonConverter.toJsonRelease(release);
    }

    public List<JsonAsset> getAssets(String repository, String tagName) {

        tagName = resolveTagName(tagName);

        log.info("Getting assets for release '{}' in repository '{}'.", tagName, repository);

        List<Asset> assets;
        try {
            assets = gitHubService.getAssets(repository, tagName);
        } catch (Exception e) {
            throw new GMFException("Could not get assets from release '" + tagName + "' in '" + repository + "'.", e);
        }

        return assets.stream().map(jsonConverter::toJsonAsset).toList();
    }

    public JsonAsset getAsset(String repository, String tagName, String assetName) {

        tagName = resolveTagName(tagName);

        log.info("Getting asset '{}' from release '{}' in repository '{}'.", assetName, tagName, repository);

        Asset asset;
        try {
            asset = gitHubService.getAsset(repository, tagName, assetName);
        } catch (Exception e) {
            throw new GMFException(
                    "Could not get asset '" + assetName + "' from release '" + tagName + "' in '" + repository + "'.",
                    e);
        }

        return jsonConverter.toJsonAsset(asset);
    }

    public InputStream getAssetContent(String repository, String tagName, String assetName) {

        tagName = resolveTagName(tagName);

        log.info("Getting content of asset '{}' from release '{}' in repository '{}'.", assetName, tagName, repository);

        try {
            return gitHubService.getAssetContent(repository, tagName, assetName);
        } catch (Exception e) {
            throw new GMFException(
                    "Could not get asset '" + assetName + "' content from release '" + tagName + "' in '" + repository
                            + "'.",
                    e);
        }
    }

    public List<JsonArchiveAsset> getArchiveAssets(String repository, String tagName, String assetName) {

        tagName = resolveTagName(tagName);

        log.info(
                "Getting archive assets from asset '{}' in release '{}' in repository '{}'.",
                assetName,
                tagName,
                repository);

        List<ArchiveAsset> archiveAssets;
        try {
            archiveAssets = gitHubService.getArchiveAssets(repository, tagName, assetName);
        } catch (Exception e) {
            throw new GMFException(
                    "Could not get archive assets from asset '" + assetName + "' from release '" + tagName + "' in '"
                            + repository + "'.",
                    e);
        }

        return archiveAssets.stream().map(jsonConverter::toJsonArchiveAsset).toList();
    }

    public InputStream getArchiveAssetContent(String repository, String tagName, String assetName, String path) {

        tagName = resolveTagName(tagName);

        log.info(
                "Getting content of archive asset '{}' from asset '{}' in release '{}' in repository '{}'.",
                path,
                assetName,
                tagName,
                repository);

        try {
            return gitHubService.getArchiveAssetContent(repository, tagName, assetName, path);
        } catch (Exception e) {
            throw new GMFException(
                    "Could not get archive asset '" + path + "' content from asset '" + assetName + "' from release '"
                            + tagName + "' in '" + repository + "'.",
                    e);
        }
    }
}
