package io.alpenglow.artoo.lance.query;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.query.closure.At;
import io.alpenglow.artoo.lance.query.closure.Count;
import io.alpenglow.artoo.lance.query.closure.Distinct;
import io.alpenglow.artoo.lance.query.closure.Every;

public sealed interface Closure<T, R> extends TryFunction1<Unit<T>, Unit<R>> permits At, Count, Distinct, Every {

}
