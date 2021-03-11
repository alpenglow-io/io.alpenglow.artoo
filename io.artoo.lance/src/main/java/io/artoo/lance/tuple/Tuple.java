package io.artoo.lance.tuple;

@SuppressWarnings("unchecked")
public interface Tuple<R extends Record & Tuple<R>> {
  Class<R> type$();

  static <A> Tuple.OfOne<A> of(final A first) { return new OfOne<>(first); }
  static <A, B> Tuple.OfTwo<A, B> of(final A first, final B second) { return new OfTwo<>(first, second); }
  static <A, B, C> Tuple.OfThree<A, B, C> of(final A first, final B second, final C third) { return new OfThree<>(first, second, third); }
  static <A, B, C, D> Tuple.OfFour<A, B, C, D> of(final A first, final B second, final C third, final D forth) { return new OfFour<>(first, second, third, forth); }
  static <A, B, C, D, E> Tuple.OfFive<A, B, C, D, E> of(final A first, final B second, final C third, final D forth, E fifth) { return new OfFive<>(first, second, third, forth, fifth); }

  record OfOne<A>(A first) implements Single<OfOne<A>, A> {
    @Override
    public Class<OfOne<A>> type$() {
      return (Class<OfOne<A>>) this.getClass();
    }
  }

  record OfTwo<A, B>(A first, B second) implements Pair<OfTwo<A, B>, A, B> {
    @Override
    public Class<OfTwo<A, B>> type$() {
      return (Class<OfTwo<A, B>>) this.getClass();
    }
  }

  record OfThree<A, B, C>(A first, B second, C third) implements Triple<OfThree<A, B, C>, A, B, C> {
    @Override
    public Class<OfThree<A, B, C>> type$() {
      return (Class<OfThree<A, B, C>>) this.getClass();
    }
  }

  record OfFour<A, B, C, D>(A first, B second, C third, D forth) implements Quadruple<OfFour<A, B, C, D>, A, B, C, D> {
    @Override
    public Class<OfFour<A, B, C, D>> type$() {
      return (Class<OfFour<A, B, C, D>>) this.getClass();
    }
  }

  record OfFive<A, B, C, D, E>(A first, B second, C third, D forth, E fifth) implements Quintuple<OfFive<A, B, C, D, E>, A, B, C, D, E> {
    @Override
    public Class<OfFive<A, B, C, D, E>> type$() {
      return (Class<OfFive<A, B, C, D, E>>) this.getClass();
    }
  }
}
