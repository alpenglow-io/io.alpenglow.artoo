package re.artoo.lance.query.cursor;

@SuppressWarnings("SwitchStatementWithTooFewBranches")
public sealed interface Reference<ELEMENT> {
  @SuppressWarnings("unchecked")
  static <ELEMENT> Reference<ELEMENT> empty() { return (Reference<ELEMENT>) Empty.None; }
  static <ELEMENT> Reference<ELEMENT> hold(ELEMENT element) { return new Hold<>(element); }
  default Reference<ELEMENT> release() {
    return switch (this) {
      case Empty ignored -> this;
      case Hold<ELEMENT> ignored -> Reference.empty();
    };
  }

  default ELEMENT retrieveThenRelease() {
    return switch (this) {
      case Hold<ELEMENT> hold when hold.element != null -> {
        ELEMENT element = hold.element;
        hold.element = null;
        yield element;
      }
      default -> null;
    };
  }

  default Reference<ELEMENT> store(ELEMENT element) {
    return new Hold<>(element);
  }

  default ELEMENT retrieve() {
    return switch (this) {
      case Hold<ELEMENT> it -> it.element;
      case Empty ignored -> null;
    };
  }

  enum Empty implements Reference<Object> {None}

  final class Hold<ELEMENT> implements Reference<ELEMENT> {
    private ELEMENT element;
    Hold(ELEMENT element) {
      this.element = element;
    }
  }
}
