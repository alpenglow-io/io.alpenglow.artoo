package io.artoo.lance.func;

import io.artoo.lance.type.Tuple;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public interface Suppl {
  @FunctionalInterface
  interface Uni<A> extends Supplier<A>, Callable<A>, Func.Uni<Void, A> {
    A tryGet() throws Throwable;

    @Override
    default A tryApply(Void unused) throws Throwable {
      return tryGet();
    }

    @Override
    default A get() {
      try {
        return tryGet();
      } catch (Throwable throwable) {
        throwable.printStackTrace();
        return null;
      }
    }

    @Override
    default A apply(Void unused) {
      return get();
    }

    @Override
    default A call() throws Exception {
      try {
        return tryGet();
      } catch (Throwable throwable) {
        throw new CallableException(throwable);
      }
    }
  }

  @FunctionalInterface
  @SuppressWarnings("rawtypes")
  interface Bi<R extends Record & Tuple.Pair> extends Uni<R> {}

  @FunctionalInterface
  @SuppressWarnings("rawtypes")
  interface Tri<R extends Record & Tuple.Triple> extends Uni<R> {}

  @SuppressWarnings("rawtypes")
  @FunctionalInterface
  interface Quad<R extends Record & Tuple.Quadruple> extends Uni<R> {}

}
