package io.artoo.frost.dropbox;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import io.artoo.lance.fetcher.Cursor;
import io.artoo.lance.query.Many;

public interface Files extends Many<FileMetadata> {
  static Files files(DbxClientV2 client, String path) {
    return new Files.Regulars(new Entries.Dropped(client, path));
  }

  final class Regulars implements Files {
    private final Entries entries;

    Regulars(final Entries entries) {this.entries = entries;}

    @Override
    public Cursor<FileMetadata> cursor() {
      return entries
        .ofType(FileMetadata.class)
        .cursor();
    }
  }
}
