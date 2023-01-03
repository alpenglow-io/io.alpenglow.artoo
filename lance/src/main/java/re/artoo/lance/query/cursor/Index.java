package re.artoo.lance.query.cursor;

sealed interface Index permits Incremental, NonIncremental {
  static Index incremental() { return new Incremental(); }
  static Index nonIncremental() { return NonIncremental.Default; }

  default Index inc() { return this; }
  default int value() { return 0; }
  default Index reset() { return this; }
}

final class Incremental implements Index {
  private int value = 0;
  private final Object lock = new Object();

  @Override
  public Index inc() {
    synchronized (lock) {
      value++;
    }
    return this;
  }

  @Override
  public int value() {
    return value;
  }

  @Override
  public Index reset() {
    synchronized (lock) {
      value = 0;
    }
    return this;
  }
}

enum NonIncremental implements Index { Default }
