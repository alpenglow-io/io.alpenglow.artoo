package oak.func.sup;

import oak.func.Functional;

import java.util.function.DoubleSupplier;

@FunctionalInterface
public interface AsFloat extends Functional.Sup {
  float get();
}
