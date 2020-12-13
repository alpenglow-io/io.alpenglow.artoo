package io.artoo.frost.scene;

import com.googlecode.lanterna.gui2.Component;
import io.artoo.lance.fetcher.Cursor;
import io.artoo.lance.query.One;

public interface Element<T> extends One<T> {
  static Element<? extends Component> empty() {
    return Default.Empty;
  }

  enum Default implements Element<Component> {
    Empty;

    @Override
    public Cursor<Component> cursor() {
      return Cursor.nothing();
    }
  }
}
