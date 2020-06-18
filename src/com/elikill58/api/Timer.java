package com.elikill58.api;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

import com.elikill58.api.game.GameAPI;

public class Timer {

    private Runnable runnable;
    private int duration;
    private final int initialDuration;
    private boolean actionBar = false;
    private Map<Integer, String> reminders = new HashMap<>();
    private Map<Integer, String> actionbarReminders = new HashMap<>();
    private boolean isRunning = false;

    private int task;

    /**
     * Create a new {@link Timer}
     *
     * @param task which will be run at the end of the time
     * @param duration The during time of timer, in second.
     */
    public Timer(int duration, Runnable task) {
        this.initialDuration = duration;
        this.duration = duration;
        this.runnable = task;
    }

    /**
     * Create a new {@link Timer}
     *
     * @param task which will be run at the end of the time
     * @param duration The during time of timer, in second.
     * @param actionBar If the player show timer in action bar
     */
    public Timer(int duration, Runnable task, boolean actionBar) {
        this(duration, task);

        this.actionBar = actionBar;
    }

    /**
     * Add a message sent at the given timing
     *
     * @param when The time in seconds when the message is sent
     * @param key The message key that will be sent in action bar
     * Placeholder:
     *            <p>
     *            %duration% is replaced by the time before the end of the timer
     *            <p>
     *            %mins% is replaced by the time (in minutes, round to below) before the end of the timer
     *            <p>
     *            %secs% is replaced by the time (in seconds) before the end of the timer
     */
    public void addReminder(int when, String key) {
        this.reminders.put(when, key);
    }

    /**
     * Add a message sent in action bar at the given timing
     * 
     * @param when The time in seconds when the message is sent
     * @param key The message key that will be sent in action bar
     * Placeholder:
     *            <p>
     *            %duration% is replaced by the time before the end of the timer
     *            <p>
     *            %mins% is replaced by the time (in minutes, round to below) before the end of the timer
     *            <p>
     *            %secs% is replaced by the time (in seconds) before the end of the timer
     */
    public void addActionbarReminder(int when, String key) {
        this.actionbarReminders.put(when, key);
    }

    /**
     * Add a message sent in action bar at between 2 given times
     *
     * @param from the min value (included)
     * @param to the max value (included)
     * @param key the message key sent
     */
    public void addActionbarRange(int from, int to, String key) {
        for(int i = from; i >= to; i--) {
            if(!actionbarReminders.containsKey(i))
                actionbarReminders.put(i, key);
        }
    }

    /**
     * Start the timer
     */
    public void start() {
        this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(GameAPI.GAME_PROVIDER.getPlugin(), () -> {
            if(actionBar && actionbarReminders.containsKey(duration)) {
                ActionBar.sendToAll(Messages.getMessage(actionbarReminders.get(duration),
                        "%duration%", String.valueOf(duration),
                        "%mins%", String.valueOf((int) Math.ceil(duration / 60)),
                        "%secs%", String.valueOf(duration % 60)));
            }

            if(reminders.containsKey(duration)) {
                GameAPI.broadcast(reminders.get(duration),
                        "%duration%", String.valueOf(duration),
                        "%mins%", String.valueOf((int) Math.ceil(duration / 60)),
                        "%secs%", String.valueOf(duration % 60));
            }

            if(duration == 0) {
                this.runnable.run();
                this.cancel();
                return;
            }

            duration--;
        }, 0, 20);
        this.isRunning = true;
    }

    /**
     * Cancel the timer
     */
    public void cancel() {
        Bukkit.getScheduler().cancelTask(this.task);
        this.isRunning = false;
        duration = initialDuration;
    }

    /**
     * Say if the timer is running
     *
     * @return true if the timer is currently running
     */
    public boolean isRunning() {
        return isRunning;
    }

    public int getTask() {
        return this.task;
    }

    /**
     * Return a new builder
     *
     * @return the builder of this class
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Runnable runnable;
        private int duration;
        private boolean actionBar = false;
        private Map<Integer, String> reminders = new HashMap<>();
        private Map<Integer, String> actionbarReminders = new HashMap<>();

        /**
         * Obligatory, it's the task which will be run at the end of task
         *
         * @param task to run at the end
         * @return this builder
         */
        public Builder task(Runnable task) {
            this.runnable = task;
            return this;
        }

        /**
         * Obligatory, the duration time in seconds
         *
         * @param duration the during time in seconds
         * @return this builder
         */
        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        /**
         * Optional, add a recall to the given time
         * <p>
         * %duration% is replace by the during time
         * <p>
         * %mins% is replace by the during time in minutes
         * <p>
         * %secs% is replace by the during time in seconds
         *
	     * @param when The time in seconds when the message is sent
	     * @param key The message key that will be sent in action bar
         * @return this builder
         */
        public Builder reminder(int when, String key) {
            this.reminders.put(when, key);
            return this;
        }

        /**
         * Optional, add a recall in action bar to the given time
         * <p>
         * %duration% is replace by the during time
         * <p>
         * %mins% is replace by the during time in minutes
         * <p>
         * %secs% is replace by the during time in seconds
         *
	     * @param when The time in seconds when the message is sent
	     * @param key The message key that will be sent in action bar
         * @return this builder
         */
        public Builder actionbarReminder(int when, String key) {
            this.actionbarReminders.put(when, key);
            return this;
        }

        /**
         * Add a message which will be showed in the timer during the time between 2 time
         *
	     * @param from the min value (included)
	     * @param to the max value (included)
	     * @param key the message key sent
         * @return this builder
         */
        public Builder actionbarRange(int from, int to, String key) {
            for(int i = from; i >= to; i--) {
                if(!actionbarReminders.containsKey(i))
                    actionbarReminders.put(i, key);
            }

            return this;
        }

        /**
         * Optional, say if the message must to be showed in action bar
		 *
         * @param actionBar true if the message is showed in action bar
         * @return this builder
         */
        public Builder actionbar(boolean actionBar) {
            this.actionBar = actionBar;
            return this;
        }

        /**
         * Create a {@link Timer}
         *
         * @return the {@link Timer}
         */
        public Timer build() {
            Timer timer = new Timer(this.duration, this.runnable, this.actionBar);
            timer.reminders = reminders;
            timer.actionbarReminders = actionbarReminders;
            return timer;
        }

        /**
         * Create a {@link Timer} and start it
         *
         * @return the {@link Timer} created
         */
        public Timer start()
        {
            Timer timer = this.build();
            timer.start();
            return timer;
        }
    }
}
