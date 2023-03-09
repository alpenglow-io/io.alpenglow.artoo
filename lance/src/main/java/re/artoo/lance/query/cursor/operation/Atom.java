package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.cursor.operation.atom.Lazy;
import re.artoo.lance.query.cursor.operation.atom.Reference;

import java.util.concurrent.atomic.AtomicBoolean;

sealed public interface Atom<ELEMENT> permits Lazy, Reference {
  static <ELEMENT> Atom<ELEMENT> reference() {
    return new Reference<>();
  }
  static <ELEMENT> Atom<ELEMENT> fetched(ELEMENT element) {
    return new Reference<>(element);
  }
  static <ELEMENT> Atom<ELEMENT> notFetched(ELEMENT element) {
    return new Reference<>(element, false);
  }
  static <ELEMENT> Atom<ELEMENT> lazyFetched(TrySupplier1<? extends ELEMENT> supplier) {
    return new Lazy<>(new Reference<>(true), supplier);
  }
  static <ELEMENT> Atom<ELEMENT> lazyNotFetched(TrySupplier1<? extends ELEMENT> supplier) {
    return new Lazy<>(new Reference<>(false), supplier);
  }
  int indexThenInc();
  int incThenIndex();
  int index();

  ELEMENT element() throws Throwable;
  ELEMENT elementThenFetched() throws Throwable;
  Atom<ELEMENT> element(ELEMENT element);
  boolean isFetched();
  default boolean isNotFetched() { return !isFetched(); }

  Atom<ELEMENT> unfetch();

}
