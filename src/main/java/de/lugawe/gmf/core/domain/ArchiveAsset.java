package de.lugawe.gmf.core.domain;

public final class ArchiveAsset {

    private String path;
    private byte[] content;

    public ArchiveAsset() {}

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
