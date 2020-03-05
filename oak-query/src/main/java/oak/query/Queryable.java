package oak.query;

import oak.func.Cons;

@FunctionalInterface
public interface Queryable<T> extends Iterable<T> {
  default void eventually(final Cons<T> eventually) {
    for (final var value : this) eventually.accept(value);
  }
}
