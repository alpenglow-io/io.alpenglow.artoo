package oak.func.sup;

import oak.func.Functional;

import java.util.function.DoubleSupplier;

@FunctionalInterface
public interface AsDouble extends DoubleSupplier, Functional.Sup {
  double get();

  @Override
  default double getAsDouble() { return this.get(); }
}
