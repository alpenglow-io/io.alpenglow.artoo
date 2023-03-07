package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TrySupplier1;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

sealed interface Atom<ELEMENT> {
  static <ELEMENT> Atom<ELEMENT> reference() {
    return new Reference<>();
  }
  static <ELEMENT> Atom<ELEMENT> reference(ELEMENT element) {
    return new Reference<>(element);
  }
  int indexThenInc();
  int incThenIndex();
  int index();

  ELEMENT element();
  ELEMENT elementThenFetched();
  Atom<ELEMENT> element(ELEMENT element);
  boolean isFetched();
  default boolean isNotFetched() { return !isFetched(); }

  final class Reference<ELEMENT> implements Atom<ELEMENT> {
    private final AtomicInteger index;
    private final AtomicReference<ELEMENT> reference;
    private final AtomicBoolean fetched;
    private Reference() {
      this(null);
    }
    private Reference(ELEMENT element) {
      this.index = new AtomicInteger(0);
      this.reference = new AtomicReference<>(element);
      this.fetched = new AtomicBoolean(true);
    }
    @Override
    public int indexThenInc() {
      return index.getAndIncrement();
    }
    @Override
    public int incThenIndex() {
      return index.incrementAndGet();
    }

    @Override
    public int index() {
      return index.get();
    }

    @Override
    public ELEMENT element() {
      return reference.get();
    }

    @Override
    public ELEMENT elementThenFetched() {
      ELEMENT element = reference.get();
      fetched.set(true);
      return element;
    }

    @Override
    public Atom<ELEMENT> element(ELEMENT element) {
      reference.set(element);
      fetched.set(false);
      return this;
    }

    @Override
    public boolean isFetched() {
      return fetched.get();
    }
  }

  final class Lazy<ELEMENT> implements Atom<ELEMENT> {
    private final Atom<ELEMENT> atom;
    private final TrySupplier1<? extends ELEMENT> lazy;
    private Lazy(Atom<ELEMENT> atom, TrySupplier1<? extends ELEMENT> lazy) {
      this.atom = atom;
      this.lazy = lazy;
    }

    @Override
    public int indexThenInc() {
      return atom.indexThenInc();
    }

    @Override
    public int incThenIndex() {
      return atom.incThenIndex();
    }

    @Override
    public int index() {
      return atom.index();
    }

    @Override
    public ELEMENT element() {
      return atom.element();
    }

    @Override
    public ELEMENT elementThenFetched() {
      return atom.elementThenFetched();
    }

    @Override
    public Atom<ELEMENT> element(ELEMENT element) {
      return atom.element();
    }

    @Override
    public boolean isFetched() {
      return false;
    }
  }
}
