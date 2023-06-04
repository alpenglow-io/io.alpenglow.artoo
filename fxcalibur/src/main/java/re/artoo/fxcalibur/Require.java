package re.artoo.fxcalibur;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.One;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;

import static java.io.InputStream.nullInputStream;

public interface Require extends One<InputStream> {
  static Require from(Module module, String file) {
    return new Resource(module, file);
  }

  static Require image(Module module, String file) {
    return new Resource(module, "img/%s".formatted(file));
  }

  static Require css(Module module, String file) {
    return new Resource(module, "css/%s".formatted(file));
  }

  default InputStream load() {
    return otherwise(nullInputStream());
  }

  record Resource(Module module, String file) implements Require {
    @Override
    public Cursor<InputStream> cursor() {
      try {
        return Cursor.maybe(module.getResourceAsStream(file));
      } catch (IOException e) {
        return Cursor.cause(new NoSuchFileException(file, null, e.getMessage()));
      }
    }
  }
}
