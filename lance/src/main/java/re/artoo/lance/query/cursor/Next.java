package re.artoo.lance.query.cursor;

import re.artoo.lance.query.FetchException;

public sealed interface Next<ELEMENT> {
  static <ELEMENT> Next<ELEMENT> success(ELEMENT element) {
    return new Success<>(element);
  }
  static <ELEMENT> Next<ELEMENT> success(int index, ELEMENT element) {
    return new Success<>(index, element);
  }
  @SuppressWarnings("unchecked")
  static <ELEMENT> Next<ELEMENT> nothing() {
    return (Next<ELEMENT>) Nothing.Companion;
  }
  static <ELEMENT> Next<ELEMENT> failure(Throwable exception) {
    return new Failure<>(FetchException.with(exception));
  }

  static <ELEMENT> Next<ELEMENT> failure(String operation, String adjective) { return new Failure<>(FetchException.withMessage("Can't fetch next element for %s cursor (no more %s elements?)".formatted(operation, adjective))); }
  static <ELEMENT> Next<ELEMENT> failure() { return new Failure<>(FetchException.withMessage("Can't fetch next element (no more elements?)")); }

  record Success<ELEMENT>(int index, ELEMENT element) implements Next<ELEMENT> {
    Success(ELEMENT element) {
      this(0, element);
    }
  }
  enum Nothing implements Next<Object> {Companion}
  record Failure<ELEMENT>(FetchException exception) implements Next<ELEMENT> {}
}
