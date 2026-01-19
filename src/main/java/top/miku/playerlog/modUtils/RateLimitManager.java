package top.miku.playerlog.modUtils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RateLimitManager {
    private final int maxPermits;      // 最大次数 (例如 10)
    private final long intervalMillis; // 时间窗口 (例如 60000ms)
    
    // 线程安全的计数器和时间戳
    private final AtomicInteger count = new AtomicInteger(0);
    private final AtomicLong lastResetTime = new AtomicLong(System.currentTimeMillis());

    public RateLimitManager(int maxPermits, long intervalMillis) {
        this.maxPermits = maxPermits;
        this.intervalMillis = intervalMillis;
    }

    /**
     * 尝试获取执行权限
     * @return true=允许执行, false=被限流
     */
    public boolean tryAcquire() {
        long now = System.currentTimeMillis();
        long last = lastResetTime.get();

        // 1. 检查是否过了时间窗口，如果是，重置计数器
        if (now - last > intervalMillis) {
            // 使用 CAS (Compare And Swap) 确保多线程安全地重置
            // 如果 lastResetTime 还是 last，就把它设为 now
            if (lastResetTime.compareAndSet(last, now)) {
                count.set(0); // 重置计数
            }
        }

        // 2. 检查当前次数是否已满
        if (count.get() < maxPermits) {
            count.incrementAndGet();
            return true;
        } else {
            return false;
        }
    }
}

