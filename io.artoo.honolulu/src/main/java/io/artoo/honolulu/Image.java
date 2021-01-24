package io.artoo.honolulu;

import io.vertx.core.buffer.Buffer;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;

public interface Image {
  ImageView apply();

  final class View implements Image {
    private final Buffer buffer;

    public View(final Buffer buffer) {this.buffer = buffer;}

    @Override
    public ImageView apply() {
      return
        new ImageView(
          new javafx.scene.image.Image(
            new ByteArrayInputStream(
              buffer.getBytes()
            )
          )
        );
    }
  }
}
