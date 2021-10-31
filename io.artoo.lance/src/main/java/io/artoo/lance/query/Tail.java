package io.artoo.lance.query;

import io.artoo.lance.func.Func.MaybeFunction;

public record Tail<T, R, F extends MaybeFunction<T, Tail<T, R, F>>>(F func, R returning) {}
