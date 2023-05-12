package com.java.lang;

import java.util.function.Supplier;

public interface Exceptionable {
  default <T> T raise(Supplier<? extends Throwable> exception) throws Throwable {
    throw exception.get();
  }
}
