package io.artoo.frost.dropbox;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import io.artoo.lance.scope.Let;

import static io.artoo.lance.scope.Let.lazy;

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
    private final Let<DbxClientV2> dropbox;

    Client(final String access) {
      dropbox = lazy(() ->
        new DbxClientV2(
          DbxRequestConfig.newBuilder("io.artoo.frost").build(),
          access
        )
      );
    }

    @Override
    public final Entries entriesOfRoot() {
      return dropbox.let(Entries::ofRoot);
    }

    @Override
    public final Files filesOfRoot() {
      return dropbox.let(Files::ofRoot);

    }

    @Override
    public final Folders foldersOfRoot() {
      return dropbox.let(Folders::ofRoot);
    }

    @Override
    public final Entries entriesOf(final String path) {
      return dropbox.let(client -> Entries.of(path, client));
    }

    @Override
    public final Folders foldersOf(final String path) {
      return dropbox.let(client -> Folders.of(path, client));
    }

    @Override
    public final Files filesOf(final String path) {
      return dropbox.let(client -> Files.of(path, client));
    }
  }
}
