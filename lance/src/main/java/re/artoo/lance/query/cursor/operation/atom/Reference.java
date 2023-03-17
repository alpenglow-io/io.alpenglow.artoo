package re.artoo.lance.query.cursor.operation.atom;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

final class Reference<ELEMENT> implements Atom<ELEMENT> {
  private final AtomicInteger index;
  private final AtomicReference<ELEMENT> reference;
  private final AtomicBoolean fetched;

  public Reference() {
    this(null);
  }
  public Reference(boolean fetched) {
    this(null, fetched);
  }
  public Reference(ELEMENT element) {
    this(element, true);
  }
  public Reference(ELEMENT element, boolean fetched) {
    this.index = new AtomicInteger(0);
    this.reference = new AtomicReference<>(element);
    this.fetched = new AtomicBoolean(fetched);
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

  @Override
  public Atom<ELEMENT> unfetch() {
    fetched.set(true);
    return this;
  }
}
