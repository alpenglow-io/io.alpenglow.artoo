package lance.func;

import java.util.concurrent.Callable;

public interface Action extends Runnable, Callable<Void> {
  void tryExecute() throws Throwable;

  @Override
  default void run() {
    try {
      tryExecute();
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
  }

  @Override
  default Void call() throws Exception {
    try {
      tryExecute();
    } catch (Throwable throwable) {
      throw new LambdaException(throwable);
    }
    return null;
  }
}
