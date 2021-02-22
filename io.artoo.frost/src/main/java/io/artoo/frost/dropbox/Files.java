package io.artoo.frost.dropbox;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.Many;

public interface Files extends Many<FileMetadata> {
  static Files ofRoot(DbxClientV2 client) {
    return new Files.Dbx(Entries.ofRoot(client));
  }

  static Files of(String path, DbxClientV2 client) {
    return new Files.Dbx(Entries.of(path, client));
  }

  final class Dbx implements Files {
    private final Entries entries;

    private Dbx(final Entries entries) {this.entries = entries;}

    @Override
    public final Cursor<FileMetadata> cursor() {
      return entries
        .ofType(FileMetadata.class)
        .cursor();
    }
  }
}
