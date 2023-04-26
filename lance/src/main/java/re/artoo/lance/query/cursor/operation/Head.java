package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.OperationException;
import re.artoo.lance.query.cursor.Fetch;

abstract sealed class Head<ELEMENT> implements Fetch<ELEMENT> permits Append, Catch, Er, Filter, Flat, Fold, Iterable, Map, Open, Or, Peek, Reduce, Rethrow {
  private final String name;
  private final String adjective;
  protected int index = -1;
  protected ELEMENT element;
  protected Throwable throwable;
  protected boolean hasElement = false;

  protected Head(String name, String adjective) {
    this.name = name;
    this.adjective = adjective;
  }

  protected Head<ELEMENT> set(int index, ELEMENT element) {
    this.index = index;
    this.element = element;
    return this;
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> let) throws Throwable {
    try {
      return hasElement ? let.invoke(index, element) : OperationException.byThrowingCantFetchNextElement(name, adjective);
    } finally {
      hasElement = false;
    }
  }

  protected Head<ELEMENT> thrown(TryIntFunction<? extends Throwable> let) throws Throwable {
    this.throwable = let.invoke(index);
    return this;
  }

  protected Head<ELEMENT> set(int index, Throwable throwable) {
    this.index = index;
    this.throwable = throwable;
    return this;
  }
}
