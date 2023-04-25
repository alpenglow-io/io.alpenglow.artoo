package re.artoo.lance.func;

import java.util.function.Supplier;

@FunctionalInterface
public interface TrySupplier1<A> extends Supplier<A>, Invocable {
  A invoke() throws Throwable;

  @Override
  default A get() {
    return attempt(this::invoke);
  }
}
