
package io.artoo.parry.eventbus.impl.codecs;


import io.artoo.parry.buffer.Buffer;
import io.artoo.parry.eventbus.MessageCodec;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class BooleanMessageCodec implements MessageCodec<Boolean, Boolean> {

  @Override
  public void encodeToWire(Buffer buffer, Boolean b) {
    buffer.appendByte((byte)(b ? 0 : 1));
  }

  @Override
  public Boolean decodeFromWire(int pos, Buffer buffer) {
    return buffer.getByte(pos) == 0;
  }

  @Override
  public Boolean transform(Boolean b) {
    // Booleans are immutable so just return it
    return b;
  }

  @Override
  public String name() {
    return "boolean";
  }

  @Override
  public byte systemCodecID() {
    return 3;
  }
}
