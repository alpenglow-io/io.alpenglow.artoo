package re.artoo.lance.query;

import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.closure.At;

public interface Closure<S, T> extends TryFunction1<S, T> {
  default <R> Closure<S, R> then(Closure<? super T, ? extends R> closure) {
    return it -> coalesce(coalesce(it, this), closure);
  }

  private <SOURCE, TARGET> TARGET coalesce(SOURCE value, TryFunction1<? super SOURCE, ? extends TARGET> function) throws Throwable {
    return value == null ? null : function.invoke(value);
  }

  static <ELEMENT> Closure<ELEMENT, ELEMENT> at(int index) {
    return new At<>(index);
  }
}

