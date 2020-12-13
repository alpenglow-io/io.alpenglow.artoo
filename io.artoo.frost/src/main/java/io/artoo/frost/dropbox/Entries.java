package io.artoo.frost.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.Metadata;
import io.artoo.lance.fetcher.Cursor;
import io.artoo.lance.query.Many;

public interface Entries extends Many<Metadata> {
  static Entries ofRoot(DbxClientV2 client) {
    return new Dropped(client);
  }

  static Entries of(DbxClientV2 client, String path) {
    return new Dropped(client, path);
  }

  final class Dropped implements Entries {
    private static final String ROOT = "";

    private final DbxClientV2 client;
    private final String path;

    Dropped(final DbxClientV2 client) { this(client, ROOT); }
    Dropped(final DbxClientV2 client, final String path) {
      this.client = client;
      this.path = path;
    }

    @Override
    public final Cursor<Metadata> cursor() {
      try {
        return Cursor.open(client
          .files()
          .listFolder(path)
          .getEntries()
          .toArray(new Metadata[0])
        );
      } catch (DbxException e) {
        e.printStackTrace();
        return Cursor.nothing();
      }
    }
  }
}
