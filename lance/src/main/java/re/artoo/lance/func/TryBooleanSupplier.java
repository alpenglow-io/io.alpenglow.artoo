package re.artoo.lance.func;

import java.util.function.BooleanSupplier;

@FunctionalInterface
public interface TryBooleanSupplier extends BooleanSupplier, Invocable {
  static TryBooleanSupplier not(final TryBooleanSupplier predicate) {
    return () -> !predicate.invoke();
  }

  boolean invoke() throws Throwable;

  default TryBooleanSupplier oppose() { return () -> !invoke(); }

  @Override
  default boolean getAsBoolean() {
    return attempt(this::invoke);
  }
}
