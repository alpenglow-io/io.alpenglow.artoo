package io.artoo.lance.func;

import io.artoo.lance.func.Func.Leftover;
import io.artoo.lance.func.Func.Nothing;

import static io.artoo.lance.func.Func.Nothing.Nil;

public interface Action extends Runnable, Func.Uni<Leftover, Nothing> {
  void tryExecute() throws Throwable;

  @Override
  default void run() {
    try {
      tryExecute();
    } catch (Throwable ignored) {
    }
  }

  @Override
  default Nothing tryApply(Leftover __) throws Throwable {
    tryExecute();
    return Nil;
  }
}
