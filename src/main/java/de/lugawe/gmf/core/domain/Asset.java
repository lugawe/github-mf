package de.lugawe.gmf.core.domain;

import java.net.URL;
import java.util.List;

public final class Asset {

    private String name;
    private URL url;
    private String contentType;
    private byte[] content;

    private List<ArchiveAsset> archiveAssets;

    public Asset() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public List<ArchiveAsset> getArchiveAssets() {
        return archiveAssets;
    }

    public void setArchiveAssets(List<ArchiveAsset> archiveAssets) {
        this.archiveAssets = archiveAssets;
    }
}
