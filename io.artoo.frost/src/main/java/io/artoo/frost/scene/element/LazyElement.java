package io.artoo.frost.scene.element;

import io.artoo.frost.scene.Element;
import io.artoo.lance.fetcher.Cursor;
import io.artoo.lance.type.Value;

public abstract class LazyElement<T> implements Element<T> {
  protected final Value<T> element = Value.lazy(this::content);

  public abstract T content();

  @Override
  public final Cursor<T> cursor() {
    return Cursor.open(element.tryGet());
  }
}
