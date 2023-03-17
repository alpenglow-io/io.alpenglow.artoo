package re.artoo.lance.query.cursor;

import re.artoo.lance.query.FetchException;

import java.util.Iterator;

public interface Probe<ELEMENT> extends Iterator<ELEMENT> {
  boolean canFetch() throws Throwable;
  default ELEMENT fetch() throws Throwable { return null; }

  @Override
  default ELEMENT next() {
    try {
      return fetch();
    } catch (Throwable throwable) {
      throw FetchException.with(throwable);
    }
  }

  @Override
  default boolean hasNext() {
    try {
      return canFetch();
    } catch (Throwable throwable) {
      throw FetchException.with(throwable);
    }
  }

  default Probe<ELEMENT> rewind() { return this; }
}

