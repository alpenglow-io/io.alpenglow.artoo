package io.artoo.lance.func;

import io.artoo.lance.tuple.Pair;
import io.artoo.lance.tuple.Quadruple;
import io.artoo.lance.tuple.Triple;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public enum Suppl {;
  @FunctionalInterface
  public interface Uni<A> extends Supplier<A>, Callable<A>, Func.Uni<Void, A> {
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
  public interface Bi<R extends Record & Pair> extends Uni<R> {}

  @FunctionalInterface
  @SuppressWarnings("rawtypes")
  public interface Tri<R extends Record & Triple> extends Uni<R> {}

  @SuppressWarnings("rawtypes")
  @FunctionalInterface
  public interface Quad<R extends Record & Quadruple> extends Uni<R> {}

}
