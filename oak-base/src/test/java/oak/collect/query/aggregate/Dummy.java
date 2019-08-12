package oak.collect.query.aggregate;

import java.util.Objects;

final class Dummy<A, B> {
  private final A id;
  private final B hash;

  private Dummy(final A id, final B hash) {
    this.id = id;
    this.hash = hash;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    var dummy = (Dummy<?, ?>) o;
    return Objects.equals(id, dummy.id) &&
      Objects.equals(hash, dummy.hash);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, hash);
  }

  @Override
  public String toString() {
    return String.format("Any{id=%s, hash=%s}", id, hash);
  }

  static <T, U> Dummy<T,U> any(final T id, final U hash) {
    return new Dummy<>(id, hash);
  }
}
