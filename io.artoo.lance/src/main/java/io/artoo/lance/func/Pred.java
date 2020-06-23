package io.artoo.lance.func;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface Pred {

  @FunctionalInterface
  interface Uni<A> extends Predicate<A>, Func.Uni<A, Boolean> {
    boolean verify(A a) throws Throwable;

    @Override
    default Boolean invoke(A a) throws Throwable {
      return verify(a);
    }

    @Override
    default boolean test(A a) {
      try {
        return verify(a);
      } catch (Throwable throwable) {
        return false;
      }
    }

    static <A> Pred.Uni<A> not(final Pred.Uni<A> predicate) {
      assert predicate != null;
      return it -> !predicate.verify(it);
    }
  }

  @FunctionalInterface
  interface Bi<A, B> extends BiPredicate<A, B>, Func.Bi<A, B, Boolean> {
    boolean verify(A a, B b) throws Throwable;

    @Override
    default Boolean invoke(A a, B b) throws Throwable {
      return verify(a, b);
    }

    @Override
    default boolean test(A a, B b) {
      try {
        return verify(a, b);
      } catch (Throwable throwable) {
        return false;
      }
    }

    static <A, B> Pred.Bi<A, B> not(final Pred.Bi<A, B> predicate) {
      assert predicate != null;
      return (a, b) -> !predicate.verify(a, b);
    }
  }

  @FunctionalInterface
  interface Tri<A, B, C> extends Func.Tri<A, B, C, Boolean> {
    boolean verify(A a, B b, C c) throws Throwable;

    @Override
    default Boolean invoke(A a, B b, C c) throws Throwable {
      return verify(a, b, c);
    }

    static <A, B, C> Pred.Tri<A, B, C> not(final Pred.Tri<A, B, C> predicate) {
      assert predicate != null;
      return (a, b, c) -> !predicate.verify(a, b, c);
    }
  }

  @FunctionalInterface
  interface Quad<A, B, C, D> extends Func.Quad<A, B, C, D, Boolean> {
    boolean verify(A a, B b, C c, D d) throws Throwable;

    @Override
    default Boolean invoke(A a, B b, C c, D d) throws Throwable {
      return verify(a, b, c, d);
    }

    static <A, B, C, D> Pred.Quad<A, B, C, D> not(final Pred.Quad<A, B, C, D> predicate) {
      assert predicate != null;
      return (a, b, c, d) -> !predicate.verify(a, b, c, d);
    }
  }

}
