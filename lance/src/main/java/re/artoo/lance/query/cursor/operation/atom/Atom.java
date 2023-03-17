package re.artoo.lance.query.cursor.operation.atom;

public sealed interface Atom<ELEMENT> permits Reference {
  static <ELEMENT> Atom<ELEMENT> reference() {
    return new Reference<>();
  }

  static <ELEMENT> Atom<ELEMENT> notFetched(ELEMENT element) {
    return new Reference<>(element, false);
  }

  int indexThenInc();

  int incThenIndex();

  int index();

  ELEMENT element();

  ELEMENT elementThenFetched();

  Atom<ELEMENT> element(ELEMENT element);

  boolean isFetched();

  default boolean isNotFetched() {
    return !isFetched();
  }

  Atom<ELEMENT> unfetch();

}
