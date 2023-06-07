package re.artoo.fxcalibur;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.One;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public interface Asset extends One<Asset> {
  static Asset image(String file) {
    return new Resource(Asset.class, "img/%s".formatted(file));
  }

  static Asset css(String file) {
    return new Resource(Asset.class, "/asset/css/%s".formatted(file));
  }

  static Asset font(String file) {
    return new Resource(Asset.class, "font/%s".formatted(file));
  }

  InputStream load() throws IOException;
  URL location();

  record Resource(Class<Asset> type, String file) implements Asset {
    @Override
    public Cursor<Asset> cursor() {
        return Cursor.open(this);
    }

    @Override
    public InputStream load() throws IOException {
      return type.getResourceAsStream(file);
    }

    @Override
    public URL location() {
      return type.getResource(file);
    }
  }
}
