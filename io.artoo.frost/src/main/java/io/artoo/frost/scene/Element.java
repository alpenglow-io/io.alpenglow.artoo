package io.artoo.frost.scene;

import com.googlecode.lanterna.gui2.Component;
import io.artoo.lance.func.Func;
import io.artoo.lance.scope.Let;

public interface Element<T> extends Let<T> {
  static Element<? extends Component> empty() {
    return Default.Empty;
  }

  enum Default implements Element<Component> {
    Empty;
    @Override
    public <R> R let(final Func.Uni<? super Component, ? extends R> func) {
      return null;
    }
  }
}
