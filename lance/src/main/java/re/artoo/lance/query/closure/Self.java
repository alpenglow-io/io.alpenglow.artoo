package re.artoo.lance.query.closure;

import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryUnaryOperator;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class Self<SELF extends Self<SELF>> {
  private final Sealant sealant;
  private final AtomicReference<SELF> reference;
  @SuppressWarnings("unchecked")
  public Self() {
    this.sealant = Sealant.readWrite(new ReentrantReadWriteLock());
    this.reference = new AtomicReference<>((SELF) this);
  }

  protected final <T> T read(TryFunction1<? super SELF, ? extends T> function) {
    try (final var sealed = sealant.sealRead()) {
      return function.apply(reference.get());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  protected final SELF write(TryUnaryOperator<SELF> operator) {
    try (final var sealed = sealant.sealWrite()) {
      return reference.updateAndGet(operator);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private sealed interface Sealant {
    static Sealant readWrite(ReadWriteLock lock) {
      return new ReadWrite(lock);
    }
    Seal sealWrite();
    Seal sealRead();

    final class ReadWrite implements Sealant {
      private final ReadWriteLock readWrite;

      public ReadWrite(ReadWriteLock readWrite) {
        this.readWrite = readWrite;
      }

      @Override
      public Seal sealWrite() {
        return Seal.selfClosable(readWrite.writeLock());
      }

      @Override
      public Seal sealRead() {
        return Seal.selfClosable(readWrite.readLock());
      }
    }
  }

  private sealed interface Seal extends Lock, AutoCloseable {
    static Seal selfClosable(Lock lock) {
      return new SelfClosable(lock);
    }
    record SelfClosable(Lock origin) implements Seal {
      public SelfClosable { origin.lock(); }

      @Override
      public void close() {
        unlock();
      }

      @Override
      public void lock() {
        origin.lock();
      }

      @Override
      public void lockInterruptibly() throws InterruptedException {
        origin.lockInterruptibly();
      }

      @Override
      public boolean tryLock() {
        return origin.tryLock();
      }

      @Override
      public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return origin.tryLock(time, unit);
      }

      @Override
      public void unlock() {
        origin.unlock();
      }

      @Override
      public Condition newCondition() {
        return origin.newCondition();
      }
    }
  }
}
