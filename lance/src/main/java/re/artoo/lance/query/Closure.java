package re.artoo.lance.query;

import re.artoo.lance.func.TryFunction1;

@FunctionalInterface
public interface Closure<S, T> extends TryFunction1<S, T> {
  default <R> Closure<S, R> then(Closure<? super T, ? extends R> closure) {
    return it -> coalesce(coalesce(it, this), closure);
  }

  private <SOURCE, TARGET> TARGET coalesce(SOURCE source, Closure<? super SOURCE, ? extends TARGET> closure) throws Throwable {
    return source == null ? null : closure.invoke(source);
  }
}

