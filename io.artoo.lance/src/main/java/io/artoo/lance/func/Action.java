package io.artoo.lance.func;

import io.artoo.lance.func.Func.Leftover;
import io.artoo.lance.func.Func.Default;

import static io.artoo.lance.func.Func.Default.Nothing;

public interface Action extends Runnable, Func.Uni<Leftover, Default> {
  void tryExecute() throws Throwable;

  @Override
  default void run() {
    try {
      tryExecute();
    } catch (Throwable ignored) {
    }
  }

  @Override
  default Default tryApply(Leftover __) throws Throwable {
    tryExecute();
    return Nothing;
  }
}
