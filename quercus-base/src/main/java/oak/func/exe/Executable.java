package oak.func.exe;

import oak.func.Functional;
import oak.func.fun.Function1;

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
