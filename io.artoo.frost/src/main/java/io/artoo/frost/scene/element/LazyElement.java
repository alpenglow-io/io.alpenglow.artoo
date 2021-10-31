package io.artoo.frost.scene.element;

import io.artoo.frost.scene.Element;
import io.artoo.lance.func.Func;
import io.artoo.lance.scope.Let;

public abstract class LazyElement<T> implements Element<T> {
  protected final Let<T> element = Let.lazy(this::content);

  public abstract T content();

  @Override
  public <R> R let(final Func.MaybeFunction<? super T, ? extends R> func) {
    return element.let(func);
  }
}
