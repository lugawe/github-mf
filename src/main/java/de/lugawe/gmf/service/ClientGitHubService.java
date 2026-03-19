package de.lugawe.gmf.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import de.lugawe.gmf.core.domain.ArchiveAsset;
import de.lugawe.gmf.core.domain.Asset;
import de.lugawe.gmf.core.domain.Release;
import de.lugawe.gmf.core.exception.GMFException;
import de.lugawe.gmf.core.service.ArchiveService;
import de.lugawe.gmf.core.service.GitHubService;
import io.quarkus.cache.CacheResult;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kohsuke.github.GHAsset;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ClientGitHubService implements GitHubService {

    private static final Logger log = LoggerFactory.getLogger(ClientGitHubService.class);

    @ConfigProperty(name = "gmf.github-token")
    private Optional<String> gitHubToken;

    @Inject
    private ArchiveService archiveService;

    private GitHub gitHub;

    public ClientGitHubService() {}

    @PostConstruct
    public void initialize() {
        try {
            if (gitHubToken.isPresent()) {
                log.info("Initializing GitHub client with OAuth token.");
                this.gitHub =
                        new GitHubBuilder().withOAuthToken(gitHubToken.get()).build();
            } else {
                log.info("Initializing GitHub client.");
                this.gitHub = new GitHubBuilder().build();
            }
        } catch (IOException e) {
            throw new GMFException("Could not build GitHub client.", e);
        }
    }

    private Asset toAsset(GHAsset asset) {
        Asset result = new Asset();
        result.setName(asset.getName());
        result.setUrl(asset.getUrl());
        result.setContentType(asset.getContentType());
        return result;
    }

    private Release toRelease(GHRelease release) {
        Release result = new Release();
        result.setName(release.getName());
        try {
            result.setAssets(
                    release.listAssets().toList().stream().map(this::toAsset).toList());
        } catch (IOException e) {
            throw new GMFException("Could not list assets for release '" + result.getName() + "'.", e);
        }
        return result;
    }

    @CacheResult(cacheName = "asset-content")
    public byte[] getAssetContent(URL url) {
        log.info("Loading asset content from '{}'.", url);
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (gitHubToken.isPresent()) {
                log.debug("Using Bearer token to retrieve asset content.");
                connection.setRequestProperty("Authorization", "Bearer " + gitHubToken.get());
            }
            connection.setRequestProperty("Accept", "application/octet-stream");
            connection.setInstanceFollowRedirects(true);
            try (InputStream inputStream = connection.getInputStream()) {
                return inputStream.readAllBytes();
            } finally {
                connection.disconnect();
            }
        } catch (Exception e) {
            throw new GMFException("Could not load asset content.", e);
        }
    }

    @Override
    @CacheResult(cacheName = "release")
    public Release getRelease(String repository, String tagName) {

        log.info("Loading release '{}' from repository '{}'.", tagName, repository);

        GHRepository repo;
        try {
            repo = gitHub.getRepository(repository);
        } catch (IOException e) {
            throw new GMFException("Could not get repository.", e);
        }
        if (repo == null) {
            throw new GMFException("Repository not found.");
        }

        GHRelease release;
        try {
            if (tagName == null || "latest".equals(tagName)) {
                release = repo.getLatestRelease();
            } else {
                release = repo.getReleaseByTagName(tagName);
            }
        } catch (IOException e) {
            throw new GMFException("Could not get release.", e);
        }
        if (release == null) {
            throw new GMFException("Release not found.");
        }

        return toRelease(release);
    }

    @Override
    @CacheResult(cacheName = "assets")
    public List<Asset> getAssets(String repository, String tagName) {

        log.info("Loading assets for release '{}' in repository '{}'.", tagName, repository);

        Release release = getRelease(repository, tagName);

        return release.getAssets();
    }

    @Override
    @CacheResult(cacheName = "asset")
    public Asset getAsset(String repository, String tagName, String assetName) {

        log.info("Loading asset '{}' from release '{}' in repository '{}'.", assetName, tagName, repository);

        List<Asset> assets = getAssets(repository, tagName);

        return assets.stream()
                .filter(asset -> assetName.equals(asset.getName()))
                .findFirst()
                .orElseThrow(() -> new GMFException("Asset not found: '" + assetName + "'."));
    }

    @Override
    public InputStream getAssetContent(String repository, String tagName, String assetName) {

        Asset asset = getAsset(repository, tagName, assetName);

        return new ByteArrayInputStream(getAssetContent(asset.getUrl()));
    }

    @Override
    @CacheResult(cacheName = "archive-assets")
    public List<ArchiveAsset> getArchiveAssets(String repository, String tagName, String assetName) {

        log.info(
                "Loading archive assets from asset '{}' in release '{}' in repository '{}'.",
                assetName,
                tagName,
                repository);

        InputStream inputStream = getAssetContent(repository, tagName, assetName);

        return archiveService.extract(inputStream);
    }

    @CacheResult(cacheName = "archive-asset")
    public ArchiveAsset getArchiveAsset(String repository, String tagName, String assetName, String path) {

        log.info(
                "Loading archive asset '{}' from asset '{}' in release '{}' in repository '{}'.",
                path,
                assetName,
                tagName,
                repository);

        List<ArchiveAsset> archiveAssets = getArchiveAssets(repository, tagName, assetName);

        return archiveAssets.stream()
                .filter(archiveAsset -> path.equals(archiveAsset.getPath()))
                .findFirst()
                .orElseThrow(() -> new GMFException("Archive asset not found: '" + path + "'."));
    }

    @Override
    public InputStream getArchiveAssetContent(String repository, String tagName, String assetName, String path) {

        ArchiveAsset archiveAsset = getArchiveAsset(repository, tagName, assetName, path);

        return new ByteArrayInputStream(archiveAsset.getContent());
    }
}
