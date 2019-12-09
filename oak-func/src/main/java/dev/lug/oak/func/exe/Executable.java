package dev.lug.oak.func.exe;

import dev.lug.oak.func.Functional;
import dev.lug.oak.func.fun.Function1;

@FunctionalInterface
public interface Executable extends Runnable, Function1<Void, Void>, Functional.Exe {
  void execute() throws Exception;

  @Override
  default void run() {
    try {
      execute();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  default Void apply(Void aVoid) {
    this.run();
    return null;
  }
}
