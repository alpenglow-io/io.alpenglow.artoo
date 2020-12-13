package io.artoo.frost.dropbox;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FolderMetadata;
import io.artoo.frost.dropbox.Entries.Dropped;
import io.artoo.lance.fetcher.Cursor;
import io.artoo.lance.query.Many;

public interface Folders extends Many<FolderMetadata> {
  static Folders fromRoot(DbxClientV2 client) {
    return new Directories(new Dropped(client));
  }

  static Folders from(DbxClientV2 client, String path) {
    return new Directories(new Dropped(client, path));
  }

  final class Directories implements Folders {
    private final Entries entries;

    public Directories(final Entries entries) {this.entries = entries;}

    @Override
    public Cursor<FolderMetadata> cursor() {
      return entries
        .ofType(FolderMetadata.class)
        .cursor();
    }
  }
}
