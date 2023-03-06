package re.artoo.lance.query.cursor.operation;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

sealed interface Atom<ELEMENT> {
  static <ELEMENT> Atom<ELEMENT> reference() {
    return new Reference<>();
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
      this.index = new AtomicInteger(0);
      this.reference = new AtomicReference<>();
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
      ELEMENT element = reference.getAndSet(null);
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
}
