package io.alpenglow.artoo.lance.scope;

public enum Default {
  Nothing;

  public boolean notEquals(Object value) {
    return !this.equals(value);
  }
}
