package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;

class Value<ELEMENT> {
  int index;
  ELEMENT element;
  boolean fetched = true;
  boolean set(ELEMENT element) {
    set(-1, element);
    return true;
  }
  boolean set(int index, ELEMENT element) {
    this.element = element;
    fetched = false;
    return true;
  }
  ELEMENT get() {
    try {
      return element;
    } finally {
      fetched = true;
    }
  }
  <NEXT> NEXT get(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) {
    return then.apply(index, get());
  }
}
