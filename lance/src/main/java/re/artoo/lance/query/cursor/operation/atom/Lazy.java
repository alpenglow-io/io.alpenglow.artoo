package re.artoo.lance.query.cursor.operation.atom;

import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.cursor.operation.Atom;

import java.util.concurrent.atomic.AtomicBoolean;

public final class Lazy<ELEMENT> implements Atom<ELEMENT> {
  private final Atom<ELEMENT> atom;
  private final AtomicBoolean initialized;
  private final TrySupplier1<? extends ELEMENT> lazy;
  public Lazy(Atom<ELEMENT> atom, TrySupplier1<? extends ELEMENT> lazy) {
    this(atom, new AtomicBoolean(false), lazy);
  }
  private Lazy(Atom<ELEMENT> atom, AtomicBoolean initialized, TrySupplier1<? extends ELEMENT> lazy) {
    this.atom = atom;
    this.initialized = initialized;
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
  public ELEMENT element() throws Throwable {
    return initialized.getAndSet(true)
      ? atom.element()
      : atom.element(lazy.invoke()).element();
  }

  @Override
  public ELEMENT elementThenFetched() throws Throwable {
    return initialized.get()
      ? atom.elementThenFetched()
      : atom.element(element()).elementThenFetched();
  }

  @Override
  public Atom<ELEMENT> element(ELEMENT element) {
    return atom.element(element);
  }

  @Override
  public boolean isFetched() {
    return atom.isFetched();
  }

  @Override
  public Atom<ELEMENT> unfetch() {
    return atom.unfetch();
  }
}
