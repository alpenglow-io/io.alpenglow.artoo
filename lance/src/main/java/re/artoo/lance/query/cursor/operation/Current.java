package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

abstract sealed class Current<ELEMENT> implements Fetch<ELEMENT> permits Append, Catch, Recover, Filter, Flat, Fold, Iterate, Map, Open, Or, Peek, Reduce, Rethrow {
  protected final Fetch<ELEMENT> fetch;
  protected final String name;
  protected final String adjective;
  protected int index = 0;
  protected ELEMENT element;
  protected Throwable throwable;
  protected boolean hasElement = false;

  protected Current(Fetch<ELEMENT> fetch, String name, String adjective) {
    this.fetch = fetch;
    this.name = name;
    this.adjective = adjective;
  }

  protected Current<ELEMENT> set(int index, ELEMENT element) {
    this.index = index;
    this.element = element;
    return this;
  }

  @Override
  public boolean hasElement() throws Throwable {
    try {
      if (!hasElement && (hasElement = fetch.hasElement())) {
        fetch.element(this::set);
      }
    } catch (Throwable throwable) {
      this.throwable = throwable;
      hasElement = true;
    }
    return hasElement;
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> apply) throws Throwable {
    try {
      hasElement = hasElement || hasElement();
      if (hasElement && throwable == null) {
        return apply.invoke(index, element);
      } else if (hasElement) {
        throw throwable;
      } else {
        return FetchException.byThrowingCantFetchNextElement(name, adjective);
      }
    } finally {
      hasElement = false;
    }
  }
}
