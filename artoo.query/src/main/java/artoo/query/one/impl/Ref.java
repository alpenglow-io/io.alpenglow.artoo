package artoo.query.one.impl;

import artoo.query.One;

public interface Ref<T> extends One<T> {
  T ref();
}
