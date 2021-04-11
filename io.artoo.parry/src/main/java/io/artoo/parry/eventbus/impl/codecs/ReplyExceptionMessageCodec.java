/*
 * Copyright (c) 2011-2019 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */

package io.artoo.parry.eventbus.impl.codecs;


import io.artoo.parry.buffer.Buffer;
import io.artoo.parry.eventbus.MessageCodec;

import io.artoo.parry.eventbus.ReplyException;
import io.artoo.parry.eventbus.ReplyFailure;
import io.artoo.parry.util.CharsetUtil;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class ReplyExceptionMessageCodec implements MessageCodec<ReplyException, ReplyException> {

  @Override
  public void encodeToWire(Buffer buffer, ReplyException body) {
    buffer.appendByte((byte)body.failureType().toInt());
    buffer.appendInt(body.failureCode());
    if (body.getMessage() == null) {
      buffer.appendByte((byte)0);
    } else {
      buffer.appendByte((byte)1);
      var encoded = body.getMessage().getBytes(CharsetUtil.UTF_8);
      buffer.appendInt(encoded.length);
      buffer.appendBytes(encoded);
    }
  }

  @Override
  public ReplyException decodeFromWire(int pos, Buffer buffer) {
    var i = (int) buffer.getByte(pos);
    var rf = ReplyFailure.fromInt(i);
    pos++;
    var failureCode = buffer.getInt(pos);
    pos += 4;
    var isNull = buffer.getByte(pos) == (byte)0;
    String message;
    if (!isNull) {
      pos++;
      var strLength = buffer.getInt(pos);
      pos += 4;
      var bytes = buffer.getBytes(pos, pos + strLength);
      message = new String(bytes, CharsetUtil.UTF_8);
    } else {
      message = null;
    }
    return new ReplyException(rf, failureCode, message);
  }

  @Override
  public ReplyException transform(ReplyException exception) {
    return exception;
  }

  @Override
  public String name() {
    return "replyexception";
  }

  @Override
  public byte systemCodecID() {
    return 15;
  }
}
