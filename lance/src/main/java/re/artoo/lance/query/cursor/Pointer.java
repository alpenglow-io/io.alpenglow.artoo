package re.artoo.lance.query.cursor;

public sealed interface Pointer {
  static Pointer neverMove() { return Never.Move; }
  static Pointer alwaysMove() { return new AlwaysMove(); }
  default Pointer next() {
    return switch (this) {
      case Never it -> it;
      case AlwaysMove it -> {
        it.index++;
        yield it;
      }
    };
  }
  default int index() {
    return switch (this) {
      case Never ignored -> 0;
      case AlwaysMove it -> it.index;
    };
  }

  default int indexNext() {
    return switch (this) {
      case Never ignored -> 0;
      case AlwaysMove it -> {
        int index = it.index;
        it.index++;
        yield index;
      }
    };
  }

  enum Never implements Pointer {Move}

  final class AlwaysMove implements Pointer {
    private int index = 0;
  }
}
