package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.FetchException;

import java.util.Iterator;

public interface Fetcher<T> extends Iterator<T> {

  T fetch() throws Throwable;

  default <R> R let(TryIntFunction1<? super T, ? extends R> function) throws Throwable {
    return function.invoke(0, fetch());
  }

  @Override
  default T next() {
    try {
      return fetch();
    } catch (Throwable throwable) {
      throw FetchException.of(throwable);
    }
  }
}

