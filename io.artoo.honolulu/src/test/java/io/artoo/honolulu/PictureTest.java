package io.artoo.honolulu;

import io.artoo.honolulu.test.Testable;
import io.vertx.core.file.FileSystem;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PictureTest implements Testable {
  private final FileSystem fileSystem = mock(FileSystem.class);

  @Test
  void shouldProvidePicture() {
    when(fileSystem.readFile(anyString())).thenReturn(ImageFutureOk);

    final var picture = Picture.from(fileSystem, "/any/path");

    picture.eventually(image -> image.onSuccess(view -> assertThat(view).isNotNull()));

    verify(fileSystem).readFile("/any/path");
  }
}
