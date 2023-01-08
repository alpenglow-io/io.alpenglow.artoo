package re.artoo.lance.query;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.closure.At;

public interface Closure<S, T> extends TryIntFunction1<S, T> {
  @Override
  default T invoke(int integer, S element) throws Throwable {
    return invoke(element);
  }

  @Deprecated(forRemoval = true)
  T invoke(S element) throws Throwable;

  default <R> Closure<S, R> then(Closure<? super T, ? extends R> closure) {
    return it -> coalesce(coalesce(it, this), closure);
  }

  private <SOURCE, TARGET> TARGET coalesce(SOURCE source, Closure<? super SOURCE, ? extends TARGET> closure) throws Throwable {
    return source == null ? null : closure.invoke(source);
  }

  static <ELEMENT> Closure<ELEMENT, ELEMENT> at(int index) {
    return new At<>(index);
  }
}

