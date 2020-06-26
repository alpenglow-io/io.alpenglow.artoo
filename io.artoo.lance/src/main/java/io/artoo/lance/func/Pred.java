package io.artoo.lance.func;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface Pred {
  static <A> Pred.Uni<A> not(final Pred.Uni<A> predicate) {
    assert predicate != null;
    return it -> !predicate.tryTest(it);
  }

  static <A, B> Pred.Bi<A, B> not(final Pred.Bi<A, B> predicate) {
    assert predicate != null;
    return (a, b) -> !predicate.tryTest(a, b);
  }

  static <A, B, C> Pred.Tri<A, B, C> not(final Pred.Tri<A, B, C> predicate) {
    assert predicate != null;
    return (a, b, c) -> !predicate.tryTest(a, b, c);
  }

  static <A, B, C, D> Pred.Quad<A, B, C, D> not(final Pred.Quad<A, B, C, D> predicate) {
    assert predicate != null;
    return (a, b, c, d) -> !predicate.tryTest(a, b, c, d);
  }

  @FunctionalInterface
  interface Uni<A> extends Predicate<A>, Func.Uni<A, Boolean> {
    boolean tryTest(A a) throws Throwable;

    @Override
    default Boolean tryApply(A a) throws Throwable {
      return tryTest(a);
    }

    @Override
    default boolean test(A a) {
      try {
        return tryTest(a);
      } catch (Throwable throwable) {
        return false;
      }
    }
  }

  @FunctionalInterface
  interface Bi<A, B> extends BiPredicate<A, B>, Func.Bi<A, B, Boolean> {
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
        return false;
      }
    }
  }

  @FunctionalInterface
  interface Tri<A, B, C> extends Func.Tri<A, B, C, Boolean> {
    boolean tryTest(A a, B b, C c) throws Throwable;

    @Override
    default Boolean tryApply(A a, B b, C c) throws Throwable {
      return tryTest(a, b, c);
    }
  }

  @FunctionalInterface
  interface Quad<A, B, C, D> extends Func.Quad<A, B, C, D, Boolean> {
    boolean tryTest(A a, B b, C c, D d) throws Throwable;

    @Override
    default Boolean tryApply(A a, B b, C c, D d) throws Throwable {
      return tryTest(a, b, c, d);
    }
  }

}
