package de.lugawe.gmf.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jakarta.enterprise.context.ApplicationScoped;

import de.lugawe.gmf.core.domain.ArchiveAsset;
import de.lugawe.gmf.core.service.ArchiveService;

@ApplicationScoped
public class ZipArchiveService implements ArchiveService {

    public ZipArchiveService() {}

    @Override
    public List<ArchiveAsset> extract(InputStream inputStream) {

        if (inputStream == null) {
            throw new NullPointerException("Parameter 'inputStream' is null.");
        }

        List<ArchiveAsset> result = new ArrayList<>();

        try (ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {

            ZipEntry zipEntry;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {

                if (zipEntry.isDirectory()) {
                    continue;
                }

                Path path = Path.of(zipEntry.getName()).normalize();

                if (path.isAbsolute() || path.startsWith("..")) {
                    continue;
                }

                ArchiveAsset archiveAsset = new ArchiveAsset();

                archiveAsset.setPath(path.toString());
                archiveAsset.setContent(zipInputStream.readAllBytes());

                result.add(archiveAsset);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to extract zip archive.", e);
        }

        return result;
    }
}
