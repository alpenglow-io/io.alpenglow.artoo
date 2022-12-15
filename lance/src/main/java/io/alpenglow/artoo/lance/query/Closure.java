package io.alpenglow.artoo.lance.query;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.query.closure.At;
import io.alpenglow.artoo.lance.query.closure.Count;
import io.alpenglow.artoo.lance.query.closure.Distinct;
import io.alpenglow.artoo.lance.query.closure.Every;
import io.alpenglow.artoo.lance.query.closure.First;
import io.alpenglow.artoo.lance.query.closure.Last;
import io.alpenglow.artoo.lance.query.closure.NotOfType;
import io.alpenglow.artoo.lance.query.closure.OfType;
import io.alpenglow.artoo.lance.query.closure.Peek;
import io.alpenglow.artoo.lance.query.closure.Single;
import io.alpenglow.artoo.lance.query.closure.Skip;
import io.alpenglow.artoo.lance.query.closure.Sum;
import io.alpenglow.artoo.lance.query.closure.Take;
import io.alpenglow.artoo.lance.query.closure.WhenType;
import io.alpenglow.artoo.lance.query.closure.WhenWhere;
import io.alpenglow.artoo.lance.query.closure.Where;

public sealed interface Closure<S, T> extends TryFunction1<S, T> permits Stateless, At, Count, Distinct, Every, First, Last, io.alpenglow.artoo.lance.query.closure.None, NotOfType, OfType, Peek, Single, Skip, io.alpenglow.artoo.lance.query.closure.Some, Sum, Take, WhenType, WhenWhere, Where {
  default <R> Closure<S, R> then(Closure<? super T, ? extends R> closure) {
    return (Stateless<S, R>) it -> coalesce(coalesce(it, this), closure);
  }

  private <SOURCE, TARGET> TARGET coalesce(SOURCE value, TryFunction1<? super SOURCE, ? extends TARGET> function) throws Throwable {
    return value == null ? null : function.invoke(value);
  }
}

/**
 * Simple trick to let defining an anonymous closure
 * @param <S> source parameter
 * @param <T> target result
 */
non-sealed interface Stateless<S, T> extends Closure<S, T> {}
