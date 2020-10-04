package io.artoo.lance.query.oper;

public final class Index {
  public int value = 0;

  private Index() {}

  public static Index indexed() { return new Index(); }
}
