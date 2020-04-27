package artoo.func;

@FunctionalInterface
public interface Exec extends Runnable, Func<Void, Void>, Functional.Exe {
  void execute();

  @Override
  default void run() {
    execute();
  }

  @Override
  default Void apply(Void aVoid) {
    this.run();
    return null;
  }
}
