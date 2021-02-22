package io.artoo.frost.dropbox;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FolderMetadata;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.Many;

public interface Folders extends Many<FolderMetadata> {
  static Folders ofRoot(DbxClientV2 client) {
    return new Folders.Dbx(Entries.ofRoot(client));
  }

  static Folders of(String path, DbxClientV2 client) {
    return new Folders.Dbx(Entries.of(path, client));
  }

  final class Dbx implements Folders {
    private final Entries entries;

    private Dbx(final Entries entries) {this.entries = entries;}

    @Override
    public Cursor<FolderMetadata> cursor() {
      return entries
        .ofType(FolderMetadata.class)
        .cursor();
    }
  }
}
