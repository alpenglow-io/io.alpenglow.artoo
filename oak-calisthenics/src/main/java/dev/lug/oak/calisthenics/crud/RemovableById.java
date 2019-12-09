package dev.lug.oak.calisthenics.crud;

import dev.lug.oak.quill.single.Nullable;

public interface RemovableById<T> extends Iterable<T> {
  Nullable<T> remove(Id id);
}
