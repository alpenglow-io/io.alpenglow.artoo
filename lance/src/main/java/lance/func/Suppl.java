package lance.func;

import lance.scope.Maybe;
import lance.tuple.Pair;
import lance.tuple.Quadruple;
import lance.tuple.Triple;

import java.util.function.Supplier;

public sealed interface Suppl {
  enum Namespace implements Suppl {}

  @FunctionalInterface
  interface MaybeSupplier<A> extends Supplier<A> {
    A tryGet() throws Throwable;

    default Maybe<A> maybeGet() {
      try {
        return Maybe.value(tryGet());
      } catch (Throwable throwable) {
        return Maybe.error(throwable);
      }
    }

    @Override
    default A get() { return maybeGet().nullable(); }
  }

  @FunctionalInterface
  @SuppressWarnings("rawtypes")
  interface MaybeBiSupplier<R extends Record & Pair> extends MaybeSupplier<R> {}

  @FunctionalInterface
  @SuppressWarnings("rawtypes")
  interface Tri<R extends Record & Triple> extends MaybeSupplier<R> {}

  @SuppressWarnings("rawtypes")
  @FunctionalInterface
  interface Quad<R extends Record & Quadruple> extends MaybeSupplier<R> {}

}
