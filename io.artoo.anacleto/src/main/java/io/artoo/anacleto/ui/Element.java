package io.artoo.anacleto.ui;

import io.artoo.lance.type.Value;

public interface Element<T> {
  T content();

  final class Attached<T> implements Element<T> {
    private final Value<T> value;
    private final Element<T> origin;

    Attached(final Element<T> origin) {
      this(Value.late(), origin);
    }
    private Attached(final Value<T> value, final Element<T> origin) {
      this.value = value;
      this.origin = origin;
    }

    @Override
    public T content() {
      return value.set(origin::content).get();
    }

    public Element<T> origin() {
      return origin;
    }
  }
}
