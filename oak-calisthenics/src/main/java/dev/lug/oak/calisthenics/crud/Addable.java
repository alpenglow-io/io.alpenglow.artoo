package dev.lug.oak.calisthenics.crud;

import dev.lug.oak.quill.single.Nullable;

public interface Addable<T> extends Iterable<T> {
  Nullable<T> add();
}
