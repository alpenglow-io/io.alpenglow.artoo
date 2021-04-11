package io.artoo.parry.eventloop;

import io.artoo.parry.util.ByteBufUtil;
import io.artoo.parry.util.Empty;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public final class DefaultChannelId implements ChannelId {
  private static final long serialVersionUID = 3884076183504074063L;
  private static final String CUSTOME_PROCESS_ID = "CUSTOME_PROCESS_ID";
  private static final String CUSTOME_MACHINE_ID = "CUSTOME_MACHINE_ID";

  private static final byte[] MACHINE_ID;
  private static final int PROCESS_ID_LEN = 4;
  private static final int PROCESS_ID;
  private static final int SEQUENCE_LEN = 4;
  private static final int TIMESTAMP_LEN = 8;
  private static final int RANDOM_LEN = 4;

  private static final AtomicInteger nextSequence = new AtomicInteger();

  /**
   * Returns a new {@link DefaultChannelId} instance.
   */
  public static DefaultChannelId newInstance() {
    return new DefaultChannelId();
  }

  static {
    var processId = -1;
    var customProcessId = CUSTOME_PROCESS_ID;
    if (customProcessId != null) {
      try {
        processId = Integer.parseInt(customProcessId);
      } catch (NumberFormatException e) {
        // Malformed input.
      }

      if (processId < 0) {
        processId = -1;
        // logger.warn("-Dio.netty.processId: {} (malformed)", customProcessId);
      }
      /*else if (logger.isDebugEnabled()) {
        // logger.debug("-Dio.netty.processId: {} (user-set)", processId);
      }*/
    }

    if (processId < 0) {
      processId = defaultProcessId();
    }

    PROCESS_ID = processId;

    byte[] machineId = null;
    var customMachineId = CUSTOME_MACHINE_ID;
/*

    if (customMachineId != null) {
      try {
        machineId = parseMAC(customMachineId);
      } catch (Exception e) {
        logger.warn("-Dio.netty.machineId: {} (malformed)", customMachineId, e);
      }
      if (machineId != null) {
        logger.debug("-Dio.netty.machineId: {} (user-set)", customMachineId);
      }
    }

    if (machineId == null) {
      machineId = defaultMachineId();
      if (logger.isDebugEnabled()) {
        logger.debug("-Dio.netty.machineId: {} (auto-detected)", MacAddressUtil.formatAddress(machineId));
      }
    }
*/

    MACHINE_ID = machineId;
  }

  private static int defaultProcessId() {
    ClassLoader loader = null;
    String value;
    try {
      loader = DefaultChannelId.class.getClassLoader();
      // Invoke java.lang.management.ManagementFactory.getRuntimeMXBean().getName()
      var mgmtFactoryType = Class.forName("java.lang.management.ManagementFactory", true, loader);
      var runtimeMxBeanType = Class.forName("java.lang.management.RuntimeMXBean", true, loader);

      var getRuntimeMXBean = mgmtFactoryType.getMethod("getRuntimeMXBean", Empty.Arrays.EMPTY_CLASSES);
      var bean = getRuntimeMXBean.invoke(null, Empty.Arrays.EMPTY_OBJECTS);
      var getName = runtimeMxBeanType.getMethod("getName", Empty.Arrays.EMPTY_CLASSES);
      value = (String) getName.invoke(bean, Empty.Arrays.EMPTY_OBJECTS);
    } catch (Throwable t) {
      //logger.debug("Could not invoke ManagementFactory.getRuntimeMXBean().getName(); Android?", t);
      try {
        // Invoke android.os.Process.myPid()
        var processType = Class.forName("android.os.Process", true, loader);
        var myPid = processType.getMethod("myPid", Empty.Arrays.EMPTY_CLASSES);
        value = myPid.invoke(null, Empty.Arrays.EMPTY_OBJECTS).toString();
      } catch (Throwable t2) {
        //logger.debug("Could not invoke Process.myPid(); not Android?", t2);
        value = "";
      }
    }

    var atIndex = value.indexOf('@');
    if (atIndex >= 0) {
      value = value.substring(0, atIndex);
    }

    int pid;
    try {
      pid = Integer.parseInt(value);
    } catch (NumberFormatException e) {
      // value did not contain an integer.
      pid = -1;
    }

/*    if (pid < 0) {
      pid = PlatformDependent.threadLocalRandom().nextInt();
      logger.warn("Failed to find the current process ID from '{}'; using a random value: {}", value, pid);
    }*/

    return pid;
  }

  private final byte[] data;
  private final int hashCode;

  private transient String shortValue;
  private transient String longValue;

  private DefaultChannelId() {
    data = new byte[MACHINE_ID.length + PROCESS_ID_LEN + SEQUENCE_LEN + TIMESTAMP_LEN + RANDOM_LEN];
    var i = 0;

    // machineId
    System.arraycopy(MACHINE_ID, 0, data, i, MACHINE_ID.length);
    i += MACHINE_ID.length;

    // processId
    i = writeInt(i, PROCESS_ID);

    // sequence
    i = writeInt(i, nextSequence.getAndIncrement());

    // timestamp (kind of)
    i = writeLong(i, Long.reverse(System.nanoTime()) ^ System.currentTimeMillis());


    // random
    var random = ThreadLocalRandom.current().nextInt();
    i = writeInt(i, random);
    assert i == data.length;

    hashCode = Arrays.hashCode(data);
  }

  private int writeInt(int i, int value) {
    data[i++] = (byte) (value >>> 24);
    data[i++] = (byte) (value >>> 16);
    data[i++] = (byte) (value >>> 8);
    data[i++] = (byte) value;
    return i;
  }

  private int writeLong(int i, long value) {
    data[i++] = (byte) (value >>> 56);
    data[i++] = (byte) (value >>> 48);
    data[i++] = (byte) (value >>> 40);
    data[i++] = (byte) (value >>> 32);
    data[i++] = (byte) (value >>> 24);
    data[i++] = (byte) (value >>> 16);
    data[i++] = (byte) (value >>> 8);
    data[i++] = (byte) value;
    return i;
  }

  @Override
  public String asShortText() {
    var shortValue = this.shortValue;
    if (shortValue == null) {
      this.shortValue = shortValue = ByteBufUtil.hexDump(data, data.length - RANDOM_LEN, RANDOM_LEN);
    }
    return shortValue;
  }

  @Override
  public String asLongText() {
    var longValue = this.longValue;
    if (longValue == null) {
      this.longValue = longValue = newLongValue();
    }
    return longValue;
  }

  private String newLongValue() {
    var buf = new StringBuilder(2 * data.length + 5);
    var i = 0;
    i = appendHexDumpField(buf, i, MACHINE_ID.length);
    i = appendHexDumpField(buf, i, PROCESS_ID_LEN);
    i = appendHexDumpField(buf, i, SEQUENCE_LEN);
    i = appendHexDumpField(buf, i, TIMESTAMP_LEN);
    i = appendHexDumpField(buf, i, RANDOM_LEN);
    assert i == data.length;
    return buf.substring(0, buf.length() - 1);
  }

  private int appendHexDumpField(StringBuilder buf, int i, int length) {
    buf.append(ByteBufUtil.hexDump(data, i, length));
    buf.append('-');
    i += length;
    return i;
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  @Override
  public int compareTo(final ChannelId o) {
    if (this == o) {
      // short circuit
      return 0;
    }
    if (o instanceof DefaultChannelId) {
      // lexicographic comparison
      final var otherData = ((DefaultChannelId) o).data;
      var len1 = data.length;
      var len2 = otherData.length;
      var len = Math.min(len1, len2);

      for (var k = 0; k < len; k++) {
        var x = data[k];
        var y = otherData[k];
        if (x != y) {
          // treat these as unsigned bytes for comparison
          return (x & 0xff) - (y & 0xff);
        }
      }
      return len1 - len2;
    }

    return asLongText().compareTo(o.asLongText());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof DefaultChannelId)) {
      return false;
    }
    var other = (DefaultChannelId) obj;
    return hashCode == other.hashCode && Arrays.equals(data, other.data);
  }

  @Override
  public String toString() {
    return asShortText();
  }
}
