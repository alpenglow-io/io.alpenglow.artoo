package io.alpenglow.artoo.lance.tuple.record;

import io.alpenglow.artoo.lance.tuple.Single;

public record OfOne<A>(A first) implements Single<A> {
}
