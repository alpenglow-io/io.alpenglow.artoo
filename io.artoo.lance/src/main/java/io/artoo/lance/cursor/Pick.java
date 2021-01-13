package io.artoo.lance.cursor;

import java.util.Arrays;

import static io.artoo.lance.scope.Nullability.nonNullable;
import static java.util.Objects.nonNull;

public interface Pick<T> extends Cursor<T> {
  @SafeVarargs
  static <R> Pick<R> every(final R... elements) {
    return new Every<>(nonNullable(elements, "elements"));
  }

  static <R> Pick<R> just(final R element) {
    return new Just<>(nonNullable(element, "element"));
  }

  static <R> Pick<R> nothing() {
    return new Nothing<>();
  }

}

final class Every<T> implements Pick<T> {
  private static final class Index {
    private int value = 0;
  }

  private final Index index = new Index();
  private final T[] elements;

  @SafeVarargs
  public Every(T... elements) {
    this.elements = elements;
  }

  @Override
  public final boolean hasNext() {
    return nonNull(elements) && elements.length > 0 && index.value < elements.length;
  }

  @Override
  public final T next() {
    return hasNext() ? elements[index.value++] : null;
  }

  @Override
  public final T fetch() {
    return next();
  }

  @Override
  public String toString() {
    return "Cursor { elements = %s }".formatted(Arrays.toString(elements));
  }
}

final class Just<R> implements Pick<R> {
  private final R element;
  private final NotFetched notFetched;

  public Just(final R element) {
    this.element = element;
    this.notFetched = new NotFetched();
  }

  @Override
  public R fetch() {
    notFetched.value = false;
    return element;
  }

  @Override
  public boolean hasNext() {
    return notFetched.value;
  }

  @Override
  public R next() {
    return fetch();
  }

  private final class NotFetched {
    private boolean value = true;
  }
}

final class Nothing<R> implements Pick<R> {
  @Override
  public R fetch() {
    return null;
  }

  @Override
  public boolean hasNext() {
    return false;
  }

  @Override
  public R next() {
    return null;
  }
}
