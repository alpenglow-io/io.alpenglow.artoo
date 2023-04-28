package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

abstract sealed class Head<ELEMENT> implements Fetch<ELEMENT> permits Append, Catch, Recover, Filter, Flat, Fold, Iterable, Map, Open, Or, Peek, Reduce, Rethrow {
  protected final String name;
  protected final String adjective;
  protected int index = -1;
  protected ELEMENT element;
  protected Throwable throwable;
  protected boolean hasElement = false;

  protected Head(String name, String adjective) {
    this.name = name;
    this.adjective = adjective;
  }

  protected TryIntFunction1<? super ELEMENT, ? extends Void> set = (index, element) -> {
    this.index = index;
    this.element = element;
    return null;
  };

  protected Head<ELEMENT> set(int index, ELEMENT element) {
    this.index = index;
    this.element = element;
    return this;
  }

  @Override
  public <NEXT> NEXT thrownAt(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    return then.invoke(index, element);
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> apply) throws Throwable {
    try {
      if (hasElement() && throwable == null) {
        return apply.invoke(index, element);
      } else if (hasElement()) {
        throw throwable;
      } else {
        return FetchException.byThrowingCantFetchNextElement(name, adjective);
      }
    } finally {
      hasElement = false;
    }
  }
}
