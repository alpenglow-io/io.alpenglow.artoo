package re.artoo.lance.query.cursor.operation.atom;

import re.artoo.lance.query.cursor.Fetch;
import re.artoo.lance.query.cursor.Fetch.Next;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

final class Reference<ELEMENT> implements Atom<ELEMENT> {
  private final AtomicReference<Next<ELEMENT>> reference;
  private final AtomicBoolean fetched;

  public Reference() {
    this(null);
  }

  public Reference(Next<ELEMENT> element) {
    this(element, true);
  }
  public Reference(Next<ELEMENT> element, boolean fetched) {
    this.reference = new AtomicReference<>(element);
    this.fetched = new AtomicBoolean(fetched);
  }

  @Override
  public Next<ELEMENT> element() {
    return reference.get();
  }

  @Override
  public Next<ELEMENT> elementThenFetched() {
    var element = reference.get();
    fetched.set(true);
    return element;
  }

  @Override
  public Atom<ELEMENT> element(Next<ELEMENT> element) {
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
