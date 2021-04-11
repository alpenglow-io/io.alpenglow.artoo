package io.artoo.parry.util;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public final class CharsetUtil {
  public static final Charset UTF_16 = StandardCharsets.UTF_16;
  public static final Charset UTF_16BE = StandardCharsets.UTF_16BE;
  public static final Charset UTF_16LE = StandardCharsets.UTF_16LE;
  public static final Charset UTF_8 = StandardCharsets.UTF_8;
  public static final Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;
  public static final Charset US_ASCII = StandardCharsets.US_ASCII;
  private static final Charset[] CHARSETS;

  public static Charset[] values() {
    return CHARSETS;
  }

  /** @deprecated */
  @Deprecated
  public static CharsetEncoder getEncoder(Charset charset) {
    return encoder(charset);
  }

  public static CharsetEncoder encoder(Charset charset, CodingErrorAction malformedInputAction, CodingErrorAction unmappableCharacterAction) {
    var e = charset.newEncoder();
    e.onMalformedInput(malformedInputAction).onUnmappableCharacter(unmappableCharacterAction);
    return e;
  }

  public static CharsetEncoder encoder(Charset charset, CodingErrorAction codingErrorAction) {
    return encoder(charset, codingErrorAction, codingErrorAction);
  }

  public static CharsetEncoder encoder(Charset charset) {
    requireNonNull(charset, "charset");
    Map<Charset, CharsetEncoder> map = InternalThreadLocalMap.get().charsetEncoderCache();
    var e = (CharsetEncoder)map.get(charset);
    if (e != null) {
      e.reset().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
    } else {
      e = encoder(charset, CodingErrorAction.REPLACE, CodingErrorAction.REPLACE);
      map.put(charset, e);
    }
    return e;
  }

  /** @deprecated */
  @Deprecated
  public static CharsetDecoder getDecoder(Charset charset) {
    return decoder(charset);
  }

  public static CharsetDecoder decoder(Charset charset, CodingErrorAction malformedInputAction, CodingErrorAction unmappableCharacterAction) {
    requireNonNull(charset, "charset");
    var d = charset.newDecoder();
    d.onMalformedInput(malformedInputAction).onUnmappableCharacter(unmappableCharacterAction);
    return d;
  }

  public static CharsetDecoder decoder(Charset charset, CodingErrorAction codingErrorAction) {
    return decoder(charset, codingErrorAction, codingErrorAction);
  }

  public static CharsetDecoder decoder(Charset charset) {
    requireNonNull(charset, "charset");
    Map<Charset, CharsetDecoder> map = InternalThreadLocalMap.get().charsetDecoderCache();
    CharsetDecoder d = (CharsetDecoder)map.get(charset);
    if (d != null) {
      d.reset().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
      return d;
    } else {
      d = decoder(charset, CodingErrorAction.REPLACE, CodingErrorAction.REPLACE);
      map.put(charset, d);
      return d;
    }
  }

  private CharsetUtil() {
  }

  static {
    CHARSETS = new Charset[]{UTF_16, UTF_16BE, UTF_16LE, UTF_8, ISO_8859_1, US_ASCII};
  }
}
