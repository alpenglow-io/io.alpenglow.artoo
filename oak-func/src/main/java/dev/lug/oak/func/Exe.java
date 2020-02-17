package dev.lug.oak.func;

@FunctionalInterface
public interface Exe extends Runnable, Fun<Void, Void>, Functional.Exe {
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
