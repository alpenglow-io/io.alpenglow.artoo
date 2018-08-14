package oak.func.sup;

import oak.func.Functional;

import java.util.function.IntSupplier;

@FunctionalInterface
public interface AsInt extends IntSupplier, Functional.Sup {
  int get();

  @Override
  default int getAsInt() {
    return get();
  }
}
