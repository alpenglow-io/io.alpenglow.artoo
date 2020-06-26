package io.artoo.lance.type;

import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Suppl;

import static io.artoo.lance.func.Func.Leftover.__;

public interface Eitherable {
  default <T> boolean either(
    final Suppl.Uni<T> operation,
    final Cons.Uni<T> result,
    final Cons.Uni<Throwable> catcher
  ) {
    try {
      final var invoked = operation.tryApply(__);
      if (invoked != null) result.tryApply(invoked);
      return true;
    } catch (Throwable cause) {
      catcher.accept(cause);
      return false;
    }
  }
}
