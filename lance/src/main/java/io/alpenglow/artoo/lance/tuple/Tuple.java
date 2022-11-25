package io.alpenglow.artoo.lance.tuple;

import io.alpenglow.artoo.lance.query.Many;

public interface Tuple {
  static <A> Tuple.OfOne<A> of(final A first) { return new OfOne<>(first); }
  static <A, B> Tuple.OfTwo<A, B> of(final A first, final B second) { return new OfTwo<>(first, second); }
  static <A, B, C> Tuple.OfThree<A, B, C> of(final A first, final B second, final C third) { return new OfThree<>(first, second, third); }
  static <A, B, C, D> Tuple.OfFour<A, B, C, D> of(final A first, final B second, final C third, final D forth) { return new OfFour<>(first, second, third, forth); }
  static <A, B, C, D, E> Tuple.OfFive<A, B, C, D, E> of(final A first, final B second, final C third, final D forth, E fifth) { return new OfFive<>(first, second, third, forth, fifth); }

  record OfOne<A>(A first) implements Single<A> {}

  record OfTwo<A, B>(A first, B second) implements Pair<A, B> {}

  record OfThree<A, B, C>(A first, B second, C third) implements Triple<A, B, C> {}

  record OfFour<A, B, C, D>(A first, B second, C third, D forth) implements Quadruple<A, B, C, D> {}

  record OfFive<A, B, C, D, E>(A first, B second, C third, D forth, E fifth) implements Quintuple<A, B, C, D, E> {}

  default Many<?> asQueryable() {
    return Type.asMany(this);
  }
}
