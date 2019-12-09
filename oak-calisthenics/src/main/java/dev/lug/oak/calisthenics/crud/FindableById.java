package dev.lug.oak.calisthenics.crud;

import dev.lug.oak.quill.single.Nullable;

public interface FindableById<T> extends Iterable<T> {
  Nullable<T> findBy(Id id);
}
