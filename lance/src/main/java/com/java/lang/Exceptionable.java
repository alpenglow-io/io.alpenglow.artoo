package com.java.lang;

import re.artoo.lance.func.TrySupplier1;

public interface Exceptionable {
  Checked checked = Checked.Exceptionally;
  Unchecked unchecked = Unchecked.Exceptionally;

  default <T> T throwing(TrySupplier1<? extends java.lang.Throwable> exception) throws java.lang.Throwable {
    throw exception.invoke();
  }

  enum Checked {
    Exceptionally;

    public <T> T throwing(TrySupplier1<? extends Exception> exception) throws java.lang.Throwable {
      throw exception.invoke();
    }
  }

  enum Unchecked {
    Exceptionally;

    public <T> T throwing(TrySupplier1<? extends RuntimeException> exception) {
      throw exception.get();
    }
  }
}
