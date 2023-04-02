package re.artoo.lance.query.cursor.operation.atom;

import re.artoo.lance.query.cursor.Next;

public sealed interface Atom<ELEMENT> permits Reference {
  static <ELEMENT> Atom<ELEMENT> reference() {
    return new Reference<>();
  }

  static <ELEMENT> Atom<ELEMENT> notFetched(Next<ELEMENT> element) {
    return new Reference<>(element, false);
  }

  Next<ELEMENT> element();

  Next<ELEMENT> elementThenFetched();

  Atom<ELEMENT> element(Next<ELEMENT> element);

  boolean isFetched();

  default boolean isNotFetched() {
    return !isFetched();
  }

  Atom<ELEMENT> unfetch();

}
