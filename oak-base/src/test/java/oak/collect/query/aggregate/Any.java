package oak.collect.query.aggregate;

import java.util.Objects;

final class Any<A, B> {
  private final A id;
  private final B hash;

  private Any(final A id, final B hash) {
    this.id = id;
    this.hash = hash;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Any<?, ?> any = (Any<?, ?>) o;
    return Objects.equals(id, any.id) &&
      Objects.equals(hash, any.hash);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, hash);
  }

  @Override
  public String toString() {
    return String.format("Any{id=%s, hash=%s}", id, hash);
  }

  static <T, U> Any<T,U> any(final T id, final U hash) {
    return new Any<>(id, hash);
  }
}
