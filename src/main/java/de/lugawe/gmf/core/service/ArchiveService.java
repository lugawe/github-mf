package de.lugawe.gmf.core.service;

import java.io.InputStream;
import java.util.List;

import de.lugawe.gmf.core.domain.ArchiveAsset;

public interface ArchiveService {

    List<ArchiveAsset> extract(InputStream inputStream);
}
