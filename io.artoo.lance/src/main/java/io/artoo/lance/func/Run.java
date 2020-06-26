package io.artoo.lance.func;

import io.artoo.lance.func.Func.Leftover;
import io.artoo.lance.func.Func.Nothing;

import static io.artoo.lance.func.Func.Nothing.Nil;

public interface Run extends Runnable, Func.Uni<Leftover, Nothing> {
  void tryRun() throws Throwable;

  @Override
  default void run() {
    try {
      tryRun();
    } catch (Throwable ignored) {
    }
  }

  @Override
  default Nothing tryApply(Leftover leftover) throws Throwable {
    tryRun();
    return Nil;
  }
}
