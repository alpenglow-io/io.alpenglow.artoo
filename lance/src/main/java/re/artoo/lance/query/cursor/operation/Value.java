package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;

class Value<ELEMENT> {
  int index;
  ELEMENT element;
  boolean set(ELEMENT element) {
    this.element = element;
    return true;
  }

  boolean set(int index, ELEMENT element) {
    this.element = element;
    return true;
  }

  <NEXT> NEXT then(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) {
    return then.apply(index, element);
  }
}
