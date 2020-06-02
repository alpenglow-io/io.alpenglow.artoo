package io.artoo.lance.value;

import io.artoo.lance.query.One;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public record Any(Object eval) {
  public Any { assert eval != null; }

  public <R extends Record> One<R> as(final @NotNull Class<R> type) {
    if (type.equals(Text.class)) return (One<R>) asText();
    if (type.equals(Char.class)) return (One<R>) asChar();
    if (type.equals(Int8.class)) return (One<R>) asInt8();
    if (type.equals(Int16.class)) return (One<R>) asInt16();
    if (type.equals(Int32.class)) return (One<R>) asInt32();
    if (type.equals(Int64.class)) return (One<R>) asInt64();
    if (type.equals(Decimal32.class)) return (One<R>) asDecimal32();
    if (type.equals(Decimal64.class)) return (One<R>) asDecimal64();

    return type.isInstance(eval) ? One.just(type.cast(eval)) : One.none();
  }

  public One<Text> asText() {
    return eval instanceof String s ? One.from(s) : One.none();
  }

  public One<Char> asChar() {
    return eval instanceof Character c ? One.from(c) : One.none();
  }

  public One<Int8> asInt8() {
    return eval instanceof Byte b ? One.from(b) : One.none();
  }

  public One<Int16> asInt16() {
    return eval instanceof Short s ? One.from(s) : One.none();
  }

  public One<Int32> asInt32() {
    return eval instanceof Integer i ? One.from(i) : One.none();
  }

  public One<Int64> asInt64() {
    return eval instanceof Long l ? One.from(l) : One.none();
  }

  public One<Decimal32> asDecimal32() {
    return eval instanceof Float f ? One.from(f) : One.none();
  }

  public One<Decimal64> asDecimal64() {
    return eval instanceof Double d ? One.from(d) : One.none();
  }
}
