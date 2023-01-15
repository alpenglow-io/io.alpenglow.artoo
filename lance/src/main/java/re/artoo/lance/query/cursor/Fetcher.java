package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.FetchException;

import java.util.Iterator;

public interface Fetcher<T> extends Iterator<T> {
  default T fetch() throws Throwable {
    return fetch((index, element) -> element);
  }
  <R> R fetch(TryIntFunction1<? super T, ? extends R> detach) throws Throwable;
  @Override
  default T next() {
    try {
      return fetch();
    } catch (Throwable throwable) {
      throw FetchException.of(throwable);
    }
  }
}

