package com.java.lang;

import re.artoo.lance.func.TrySupplier1;

public interface Exceptionable {
  default <T> T raise(TrySupplier1<? extends Throwable> exception) throws Throwable {
    throw exception.invoke();
  }
}
