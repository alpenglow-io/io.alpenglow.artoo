package re.artoo.lance.scope;

public enum Default {
  Nothing, Flushed;

  public boolean notEquals(Object value) {
    return !this.equals(value);
  }
}
