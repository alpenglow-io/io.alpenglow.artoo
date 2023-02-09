package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.FetchException;

import java.util.Iterator;

public interface Head<T> extends Iterator<T> {
  default T scroll() throws Throwable {
    return scroll((index, element) -> element);
  }
  <R> R scroll(TryIntFunction1<? super T, ? extends R> fetch) throws Throwable;

  @Override
  default T next() {
    try {
      return scroll();
    } catch (Throwable throwable) {
      throw FetchException.of(throwable);
    }
  }

  default Head<T> head() { return this; }
}

