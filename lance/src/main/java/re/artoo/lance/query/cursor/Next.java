package re.artoo.lance.query.cursor;

import re.artoo.lance.query.FetchException;

public sealed interface Next<ELEMENT> {
  static <ELEMENT> Next<ELEMENT> success(ELEMENT element) {
    return new Success<>(element);
  }
  @SuppressWarnings("unchecked")
  static <ELEMENT> Next<ELEMENT> nothing() {
    return (Next<ELEMENT>) Nothing.Companion;
  }
  static <ELEMENT> Next<ELEMENT> failure(Throwable exception) {
    return new Failure<>(FetchException.with(exception));
  }
}

record Success<ELEMENT>(ELEMENT element) implements Next<ELEMENT> {}
enum Nothing implements Next<Object> {Companion}
record Failure<ELEMENT>(FetchException exception) implements Next<ELEMENT> {}
