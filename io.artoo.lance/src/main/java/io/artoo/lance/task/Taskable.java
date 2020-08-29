package io.artoo.lance.task;

import io.artoo.lance.func.Suppl;

public interface Taskable<T> {
  Suppl.Uni<T> task();
}
