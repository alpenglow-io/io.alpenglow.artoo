package io.artoo.honolulu;

import io.artoo.lance.fetcher.Cursor;
import io.artoo.lance.query.Many;
import io.vertx.core.Future;
import io.vertx.core.file.FileSystem;
import javafx.scene.image.ImageView;

public interface Pictures extends Many<Future<ImageView>> {
  static Pictures from(FileSystem fileSystem, String... paths) {
    return new Local(fileSystem, paths);
  }

  final class Local implements Pictures {
    private final FileSystem fileSystem;
    private final String[] paths;

    private Local(final FileSystem fileSystem, final String[] paths) {
      this.fileSystem = fileSystem;
      this.paths = paths;
    }

    @Override
    public Cursor<Future<ImageView>> cursor() {
      return Many.from(paths)
        .select(path -> Picture.from(fileSystem, path))
        .selection(picture -> picture)
        .cursor();
    }
  }
}
