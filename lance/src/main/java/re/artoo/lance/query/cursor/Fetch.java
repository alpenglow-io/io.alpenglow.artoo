package re.artoo.lance.query.cursor;

import re.artoo.lance.query.cursor.Fetch.Next;

import java.util.Iterator;

public interface Fetch<ELEMENT> extends Iterator<Next<ELEMENT>> {
  sealed interface Next<ELEMENT> {
    static <ELEMENT> Next<ELEMENT> of(ELEMENT element) {
      return new Just<>(element);
    }
    static <ELEMENT> Next<ELEMENT> of(int index, ELEMENT element) {
      return new Indexed<>(index, element);
    }
    ELEMENT element();
    record Indexed<ELEMENT>(int index, ELEMENT element) implements Next<ELEMENT> {}
    record Just<ELEMENT>(ELEMENT element) implements Next<ELEMENT> {}
  }
}

