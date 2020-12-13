package io.artoo.frost.dropbox;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import io.artoo.lance.type.Value;

import static io.artoo.lance.type.Value.lazy;

public interface Dropbox {
  static Dropbox client(final String accessToken) {
    return new Client(accessToken);
  }

  Entries entriesOfRoot();

  Files filesOfRoot();

  Folders foldersOfRoot();

  Entries entriesOf(String path);

  Folders foldersOf(String path);

  Files filesOf(String path);

  final class Client implements Dropbox {
    private final Value<DbxClientV2> client;

    Client(final String access) {
      client = lazy(() ->
        new DbxClientV2(
          DbxRequestConfig.newBuilder("io.artoo.frost").build(),
          access
        )
      );
    }

    @Override
    public final Entries entriesOfRoot() {
      return new Entries.Dropped(client.tryGet());
    }

    @Override
    public final Files filesOfRoot() {
      return
        new Files.Regulars(
          new Entries.Dropped(client.tryGet())
        );
    }

    @Override
    public final Folders foldersOfRoot() {
      return
        new Folders.Directories(
          new Entries.Dropped(
            client.tryGet()
          )
        );
    }

    @Override
    public final Entries entriesOf(final String path) {
      return
        new Entries.Dropped(
          client.tryGet(),
          path
        );
    }

    @Override
    public final Folders foldersOf(final String path) {
      return
        new Folders.Directories(
          new Entries.Dropped(
            client.tryGet(),
            path
          )
        );
    }

    @Override
    public final Files filesOf(final String path) {
      return
        new Files.Regulars(
          new Entries.Dropped(
            client.tryGet(),
            path
          )
        );
    }
  }
}
