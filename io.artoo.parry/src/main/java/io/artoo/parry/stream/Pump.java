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

package io.artoo.parry.stream;

import io.artoo.parry.stream.impl.PumpImpl;

public interface Pump {

  /**
   * Create a new {@code Pump} with the given {@code ReadStream} and {@code WriteStream}
   *
   * @param rs  the read stream
   * @param ws  the write stream
   * @return the pump
   */
  static <T> Pump pump(ReadStream<T> rs, WriteStream<T> ws) {
    return new PumpImpl<>(rs, ws);
  }

  /**
   * Create a new {@code Pump} with the given {@code ReadStream} and {@code WriteStream} and
   * {@code writeQueueMaxSize}
   *
   * @param rs  the read stream
   * @param ws  the write stream
   * @param writeQueueMaxSize  the max size of the write queue
   * @return the pump
   */
  static <T> Pump pump(ReadStream<T> rs, WriteStream<T> ws, int writeQueueMaxSize) {
    return new PumpImpl<>(rs, ws, writeQueueMaxSize);
  }

  /**
   * Set the write queue max size to {@code maxSize}
   *
   * @param maxSize  the max size
   * @return a reference to this, so the API can be used fluently
   */
  Pump setWriteQueueMaxSize(int maxSize);

  /**
   * Start the Pump. The Pump can be started and stopped multiple times.
   *
   * @return a reference to this, so the API can be used fluently
   */
  Pump start();

  /**
   * Stop the Pump. The Pump can be started and stopped multiple times.
   *
   * @return a reference to this, so the API can be used fluently
   */
  Pump stop();

  /**
   * Return the total number of items pumped by this pump.
   */
  int numberPumped();

}
