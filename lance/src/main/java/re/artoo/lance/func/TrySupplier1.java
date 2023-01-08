package re.artoo.lance.func;

import java.util.function.Supplier;

@FunctionalInterface
public interface TrySupplier1<A> extends Supplier<A> {
  A invoke() throws Throwable;
  @Override
  default A get() {
    try {
      return invoke();
    } catch (Throwable throwable) {
      throw new InvokeException(throwable);
    }
  }
}
