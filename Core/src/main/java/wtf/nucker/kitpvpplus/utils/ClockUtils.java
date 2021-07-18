package wtf.nucker.kitpvpplus.utils;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import wtf.nucker.kitpvpplus.KitPvPPlus;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * A useful class for using time in your plugins
 *
 * @author Nucker
 * @see java.util.concurrent.TimeUnit This TimeUnit class for more usful time converstion
 */
public class ClockUtils {

    private static final Plugin plugin = KitPvPPlus.getInstance();

    /**
     * A method to convert ticks to seconds
     *
     * @param ticks the amount of ticks you want to convert
     * @return the ticks provided as seconds
     */
    public static long ticksToSeconds(long ticks) {
        return ticks * 20;
    }

    /**
     * A method to convert seconds to ticks
     *
     * @param seconds the amount of seconds you want to convert
     * @return the seconds provided as ticks
     */
    public static long secondsToTicks(long seconds) {
        return seconds / 20;
    }

    public static String formatSeconds(long seconds) {
        if (seconds >= 86400 /* One day */) {
            return TimeUnit.SECONDS.toDays(seconds) + " day(s)";
        }

        if (seconds >= 3600 /* One hour */) {
            return TimeUnit.SECONDS.toHours(seconds) + " hour(s)";
        }

        if (seconds >= 60 /* One minute */) {
            return TimeUnit.SECONDS.toMinutes(seconds) + " minute(s)";
        }

        return seconds + " second(s)";
    }

    /**
     * A method to run countdowns easily in your plugins
     *
     * @param amount      The amount of seconds you want to countdown from
     * @param runningCode The code ran every time one second of the countdown passes (includes the last tick).
     *                    The consumer extends the {@link CountingRunnable} class.
     * @param endCode     The code ran at the end when the countdown reaches its final tick
     *                    The consumer extends the {@link CountingRunnable} class.
     */
    public static void countDown(int amount, Consumer<CountingRunnable> runningCode, Consumer<CountingRunnable> endCode) {
        new BukkitRunnable() {
            int i = amount;
            final CountingRunnable countingRunnable = new CountingRunnable(i, this);

            public void run() {
                if (i < 0) this.cancel();
                if (i == 0) {
                    endCode.accept(countingRunnable);
                    this.cancel();
                }

                runningCode.accept(countingRunnable);
                i--;
                countingRunnable.setAmount(i);
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    /**
     * A method to run count ups easily in your plugins
     *
     * @param amount      The amount of seconds you want to countdown from
     * @param runningCode The code ran every time one second of the countdown passes (includes the last tick).
     *                    The consumer extends the {@link CountingRunnable} class.
     * @param endCode     The code ran at the end when the countdown reaches its final tick
     *                    The consumer extends the {@link CountingRunnable} class.
     */
    public static void countUp(int amount, Consumer<CountingRunnable> runningCode, Consumer<CountingRunnable> endCode) {
        new BukkitRunnable() {
            int i = 0;
            final CountingRunnable countingRunnable = new CountingRunnable(i, this);

            public void run() {
                if (i == amount) {
                    endCode.accept(countingRunnable);
                    this.cancel();
                }

                runningCode.accept(countingRunnable);
                i++;
                countingRunnable.setAmount(i);
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    /**
     * Runs your code after a delay
     *
     * @param delay   the delay between you calling the method and running the code.
     * @param runCode The code ran when the delay ends. The consumer extends the {@link CountingRunnable} class.
     */
    public static void runCodeLater(int delay, Consumer<BukkitRunnable> runCode) {
        ClockUtils.countDown(delay, c -> {
        }, c -> {
            if (c.getAmount() == 0) {
                runCode.accept(c.getRunnable());
            }
        });
    }

    /**
     * Runs code after delay on repeat until.
     *
     * @param delay   The delay between each code run in seconds
     * @param runCode The code ran when the delay is completed
     * @return The runnable instance so you can cancel it if you want
     */
    public static BukkitRunnable runInterval(int delay, Consumer<CountingRunnable> runCode) {
        BukkitRunnable runnable = new BukkitRunnable() {
            int i = delay;
            final CountingRunnable countingRunnable = new CountingRunnable(i, this);

            public void run() {
                if (i <= 0) {
                    this.i = delay;
                    runCode.accept(countingRunnable);
                }
                i--;
                countingRunnable.setAmount(i);
            }
        };

        runnable.runTaskTimer(plugin, 0L, 20L);
        return runnable;
    }


    /**
     * A class to get the amount a runnable is on as well as the runnable instance
     */
    public static class CountingRunnable {

        private final BukkitRunnable runnable;
        private int amount;

        /**
         * Private as its only meant to be used in {@link ClockUtils}
         *
         * @param amount   The amount the runnable is currently on
         * @param runnable An instance of the runnable
         */
        private CountingRunnable(int amount, BukkitRunnable runnable) {
            this.runnable = runnable;
            this.amount = amount;
        }

        /**
         * Get instance of the runnable
         *
         * @return the instance of the runnable
         */
        public BukkitRunnable getRunnable() {
            return runnable;
        }

        /**
         * Get the count the runnable is on
         *
         * @return the amount the runnable is on
         */
        public int getAmount() {
            return amount;
        }

        @SuppressWarnings("UnusedReturnValue")
        private int setAmount(int newAmount) {
            this.amount = newAmount;
            return this.getAmount();
        }
    }
}
