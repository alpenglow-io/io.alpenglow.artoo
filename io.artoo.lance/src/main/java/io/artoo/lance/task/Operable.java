package io.artoo.lance.task;

import io.artoo.lance.func.Func;

public interface Operable<T, R> extends Eventual<R>, Func.Uni<T, R> {
}
