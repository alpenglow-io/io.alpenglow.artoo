package io.alpenglow.artoo.lance.query;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.query.closure.At;
import io.alpenglow.artoo.lance.query.closure.Count;
import io.alpenglow.artoo.lance.query.closure.Distinct;
import io.alpenglow.artoo.lance.query.closure.Every;

public interface Closure<T, R> extends TryFunction1<Unit<T>, Unit<R>> permits At, Count, Distinct, Every {
  default <V> Closure<T, V> then(Closure<R, V> closure) {
    return it -> closure.invoke(invoke(it));
  }

  public static void main(String[] args) {
    Closure<Integer, Float> closure = it -> Unit.of(Float.valueOf(it.value()));
  }
}
