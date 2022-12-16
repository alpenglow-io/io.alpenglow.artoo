package io.alpenglow.artoo.lance.query;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.query.closure.*;

public sealed interface Closure<S, T> extends TryFunction1<S, T> permits Anonymous, At, Count, Distinct, Every, First, Last, io.alpenglow.artoo.lance.query.closure.None, NotOfType, OfType, Peek, Single, Skip, io.alpenglow.artoo.lance.query.closure.Some, Sum, Take, WhenType, WhenWhere, Where {
  default <R> Closure<S, R> then(Closure<? super T, ? extends R> closure) {
    return (Anonymous<S, R>) it -> coalesce(coalesce(it, this), closure);
  }

  private <SOURCE, TARGET> TARGET coalesce(SOURCE value, TryFunction1<? super SOURCE, ? extends TARGET> function) throws Throwable {
    return value == null ? null : function.invoke(value);
  }
}

