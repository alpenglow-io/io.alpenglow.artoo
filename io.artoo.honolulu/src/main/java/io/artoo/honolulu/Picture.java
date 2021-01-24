package io.artoo.honolulu;

import io.artoo.lance.fetcher.Cursor;
import io.artoo.lance.query.One;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;

public interface Picture extends One<Future<ImageView>> {
  static Picture from(FileSystem fileSystem, final String path) {
    return new Local(fileSystem, path);
  }

  final class Local implements Picture {
    private final FileSystem fileSystem;
    private final String path;

    public Local(final FileSystem fileSystem, final String path) {
      this.fileSystem = fileSystem;
      this.path = path;
    }

    @Override
    public Cursor<Future<ImageView>> cursor() {
      return Cursor.open(
        fileSystem.readFile(path)
          .map(Buffer::getBytes)
          .map(ByteArrayInputStream::new)
          .map(Image::new)
          .map(ImageView::new)
      );
    }
  }
}
