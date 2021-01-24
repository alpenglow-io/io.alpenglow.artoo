package io.artoo.honolulu;

import io.artoo.honolulu.test.Testable;
import io.vertx.core.Future;
import io.vertx.core.file.FileSystem;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class PicturesTest implements Testable {
  private final FileSystem fileSystem = mock(FileSystem.class);

  @Test
  void shouldProvidePictures() {
    final var pictures = Pictures.from(fileSystem, "/any/path1", "/any/path2");

    await(() ->
      pictures
        .or("Can't succeed", IllegalStateException::new)
        .all(Future::succeeded)
        .eventually()
    );
  }
}
