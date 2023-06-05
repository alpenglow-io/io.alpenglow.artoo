package re.artoo.fxcalibur;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.One;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.NoSuchFileException;

import static java.io.InputStream.nullInputStream;

public interface Asset extends One<Asset> {
  static Asset from(Module module, String file) {
    return new Resource(module, file);
  }

  static Asset image(Module module, String file) {
    return new Resource(module, "img/%s".formatted(file));
  }

  static Asset css(Module module, String file) {
    return new Resource(module, "css/%s".formatted(file));
  }
  static Asset font(Module module, String file) {
    return new Resource(module, "font/%s".formatted(file));
  }

  InputStream load() throws IOException;
  URL location();

  record Resource(Module module, String file) implements Asset {
    @Override
    public Cursor<Asset> cursor() {
        return Cursor.open(this);
    }

    @Override
    public InputStream load() throws IOException {
      return module.getResourceAsStream(file);
    }

    @Override
    public URL location() {
      return module.getClassLoader().getResource(file);
    }
  }
}
