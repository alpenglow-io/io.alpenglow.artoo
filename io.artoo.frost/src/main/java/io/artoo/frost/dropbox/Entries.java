package io.artoo.frost.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.Metadata;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.Many;

public interface Entries extends Many<Metadata> {
  static Entries ofRoot(DbxClientV2 client) {
    return new Dropbox(client);
  }

  static Entries of(String path, DbxClientV2 client) {
    return new Dropbox(path, client);
  }

  final class Dropbox implements Entries {
    private static final String ROOT = "";

    private final DbxClientV2 client;
    private final String path;

    private Dropbox(final DbxClientV2 client) { this(ROOT, client); }
    private Dropbox(final String path, final DbxClientV2 client) {
      this.client = client;
      this.path = path;
    }

    @Override
    public final Cursor<Metadata> cursor() {
      try {
        return
          Cursor.iteration(
            client
              .files()
              .listFolder(path)
              .getEntries()
              .iterator()
          );
      } catch (DbxException e) {
        e.printStackTrace();
        return Cursor.nothing();
      }
    }
  }
}
