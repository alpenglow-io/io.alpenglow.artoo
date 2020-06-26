package io.artoo.lance.func;

import io.artoo.lance.func.Func.Leftover;
import io.artoo.lance.type.Tuple;

import java.util.function.Supplier;

public interface Suppl {
  interface Uni<A> extends Supplier<A>, Func.Uni<Leftover, A> {
    A tryGet() throws Throwable;

    @Override
    default A tryApply(Leftover __) throws Throwable {
      return tryGet();
    }

    @Override
    default A get() {
      try {
        return tryGet();
      } catch (Throwable throwable) {
        return null;
      }
    }

    @Override
    default A apply(Leftover __) {
      return get();
    }
  }

  @SuppressWarnings("rawtypes")
  interface Bi<R extends Record & Tuple.Pair> extends Supplier<R>, Func.Uni<Leftover, R> {
    R tryGet() throws Throwable;

    @Override
    default R tryApply(Leftover __) throws Throwable {
      return tryGet();
    }

    @Override
    default R get() {
      try {
        return tryGet();
      } catch (Throwable throwable) {
        return null;
      }
    }

    @Override
    default R apply(Leftover __) {
      return get();
    }
  }

  @SuppressWarnings("rawtypes")
  interface Tri<R extends Record & Tuple.Triple> extends Supplier<R>, Func.Uni<Leftover, R> {
    R tryGet() throws Throwable;

    @Override
    default R tryApply(Leftover __) throws Throwable {
      return tryGet();
    }

    @Override
    default R get() {
      try {
        return tryGet();
      } catch (Throwable throwable) {
        return null;
      }
    }

    @Override
    default R apply(Leftover __) {
      return get();
    }
  }

  @SuppressWarnings("rawtypes")
  interface Quad<R extends Record & Tuple.Quadruple> extends Supplier<R>, Func.Uni<Leftover, R> {
    R tryGet() throws Throwable;

    @Override
    default R tryApply(Leftover __) throws Throwable {
      return tryGet();
    }

    @Override
    default R get() {
      try {
        return tryGet();
      } catch (Throwable throwable) {
        return null;
      }
    }

    @Override
    default R apply(Leftover __) {
      return get();
    }
  }
}
