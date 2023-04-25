package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;

class Next<ELEMENT> {
  private int index;
  private ELEMENT element;
  private Throwable throwable;
  private boolean hasElement;

  Next<ELEMENT> set(int index, ELEMENT element) {
    this.index = index;
    this.element = element;
    return this;
  }

  Next<ELEMENT> set(Throwable throwable) {
    this.throwable = throwable;
    return this;
  }

  <NEXT> NEXT let(TryIntFunction1<? super ELEMENT, ? extends NEXT> let) throws Throwable {
    try {
      return let.invoke(index, element);
    } finally {
      element = null;
      index = -1;
      hasElement = false;
    }
  }

  boolean bet()

  Next<ELEMENT> reset() {
    element = null;
    throwable = null;
    hasElement = false;
    return this;
  }
}
