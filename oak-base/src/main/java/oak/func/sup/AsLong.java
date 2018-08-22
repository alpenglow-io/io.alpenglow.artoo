package oak.func.sup;

import oak.func.Functional;

import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

@FunctionalInterface
public interface AsLong extends LongSupplier, Functional.Sup {
  long get();

  @Override
  default long getAsLong() {
    return get();
  }
}
