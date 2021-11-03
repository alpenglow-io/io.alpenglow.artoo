package io.artoo.honolulu;

import io.artoo.lance.func.Func;
import io.artoo.lance.scope.Let;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public interface File extends Let<InputStream> {

  final class Input implements File {
    private final Path path;
    private final Let<InputStream> input;

    public Input(final Path path) {
      this.path = path;
      this.input = Let.lazy(this::inputStream);
    }

    private InputStream inputStream() throws IOException {
      return new ByteArrayInputStream(Files.readAllBytes(path));
    }

    @Override
    public <R> R let(final Func.MaybeFunction<? super InputStream, ? extends R> func) {
      return input.let(func);
    }
  }
}
