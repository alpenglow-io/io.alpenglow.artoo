package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.cursor.operation.atom.Lazy;
import re.artoo.lance.query.cursor.operation.atom.Eager;

import java.util.concurrent.atomic.AtomicBoolean;

sealed public interface Atom<ELEMENT> permits Lazy, Eager {
  static <ELEMENT> Atom<ELEMENT> reference() {
    return new Eager<>();
  }
  static <ELEMENT> Atom<ELEMENT> reference(ELEMENT element) {
    return new Eager<>(element);
  }
  static <ELEMENT> Atom<ELEMENT> lazy(TrySupplier1<? extends ELEMENT> supplier) {
    return new Lazy<>(new Eager<>(), new AtomicBoolean(false), supplier);
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
