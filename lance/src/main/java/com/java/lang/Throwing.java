package com.java.lang;

import re.artoo.lance.func.TrySupplier1;

public interface Throwing {
  Checked checked = Checked.Exceptionally;
  Unchecked unchecked = Unchecked.Exceptionally;

  enum Checked {
    Exceptionally;

    public <T> T withThrow(TrySupplier1<? extends Exception> exception) throws Throwable {
      throw exception.invoke();
    }
  }

  enum Unchecked {
    Exceptionally;

    public <T> T withThrow(TrySupplier1<? extends RuntimeException> exception) {
      throw exception.get();
    }
  }

  default <T> T throwing(TrySupplier1<? extends Throwable> exception) throws Throwable {
    throw exception.invoke();
  }
}
