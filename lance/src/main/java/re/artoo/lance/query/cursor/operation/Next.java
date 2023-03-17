package re.artoo.lance.query.cursor.operation;

public sealed interface Next<ELEMENT> {
  static <ELEMENT> Next<ELEMENT> success(ELEMENT element) {
    return new Success<>(element);
  }
  @SuppressWarnings("unchecked")
  static <ELEMENT> Next<ELEMENT> nothing() {
    return (Next<ELEMENT>) Nothing.Companion;
  }
  static <ELEMENT> Next<ELEMENT> failure(Throwable exception) {
    return new Failure<>(exception);
  }

  default ELEMENT element() { return null; }
}

record Success<ELEMENT>(ELEMENT element) implements Next<ELEMENT> {}
enum Nothing implements Next<Object> {Companion}
record Failure<ELEMENT>(Throwable exception) implements Next<ELEMENT> {}
