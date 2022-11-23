package io.artoo.lance.func;

import io.artoo.lance.scope.Maybe;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public sealed interface Pred {
  static <A> MaybePredicate<A> not(final MaybePredicate<A> predicate) {
    return it -> !predicate.tryTest(it);
  }

  static <A, B> Pred.Bi<A, B> not(final Pred.Bi<A, B> predicate) {
    return (a, b) -> !predicate.tryTest(a, b);
  }

  static <A, B, C> Pred.Tri<A, B, C> not(final Pred.Tri<A, B, C> predicate) {
    return (a, b, c) -> !predicate.tryTest(a, b, c);
  }

  static <A, B, C, D> Pred.Quad<A, B, C, D> not(final Pred.Quad<A, B, C, D> predicate) {
    return (a, b, c, d) -> !predicate.tryTest(a, b, c, d);
  }

  static <A, B, C, D, E> Pred.Quin<A, B, C, D, E> not(final Pred.Quin<A, B, C, D, E> predicate) {
    return (a, b, c, d, e) -> !predicate.tryTest(a, b, c, d, e);
  }

  enum Namespace implements Pred {}

  @FunctionalInterface
  interface MaybePredicate<A> extends Predicate<A> {
    boolean tryTest(A a) throws Throwable;

    default Maybe<Boolean> verify(A a) {
      try {
        return Maybe.value(tryTest(a));
      } catch (Throwable throwable) {
        return Maybe.error(throwable);
      }
    }

    @Override
    default boolean test(A a) {
      return verify(a).or(() -> false).nullable();
    }

    default MaybePredicate<A> not() {
      return it -> !this.tryTest(it);
    }
  }

  @FunctionalInterface
  interface Bi<A, B> extends BiPredicate<A, B>, Func.MaybeBiFunction<A, B, Boolean> {
    boolean tryTest(A a, B b) throws Throwable;

    @Override
    default Boolean tryApply(A a, B b) throws Throwable {
      return tryTest(a, b);
    }

    @Override
    default boolean test(A a, B b) {
      try {
        return tryTest(a, b);
      } catch (Throwable throwable) {
        throwable.printStackTrace();
        return false;
      }
    }
  }

  @FunctionalInterface
  interface Tri<A, B, C> extends Func.MaybeTriFunction<A, B, C, Boolean> {
    boolean tryTest(A a, B b, C c) throws Throwable;

    @Override
    default Boolean tryApply(A a, B b, C c) throws Throwable {
      return tryTest(a, b, c);
    }
  }

  @FunctionalInterface
  interface Quad<A, B, C, D> extends Func.MaybeQuadFunction<A, B, C, D, Boolean> {
    boolean tryTest(A a, B b, C c, D d) throws Throwable;

    @Override
    default Boolean tryApply(A a, B b, C c, D d) throws Throwable {
      return tryTest(a, b, c, d);
    }
  }

  @FunctionalInterface
  interface Quin<A, B, C, D, E> extends Func.Quin<A, B, C, D, E, Boolean> {
    boolean tryTest(A a, B b, C c, D d, E e) throws Throwable;

    @Override
    default Boolean tryApply(A a, B b, C c, D d, E e) throws Throwable {
      return tryTest(a, b, c, d, e);
    }
  }

}
