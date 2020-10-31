package com.elikill58.api.game.phase;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

import com.elikill58.api.ActionBar;
import com.elikill58.api.Messages;
import com.elikill58.api.game.GameAPI;

public class PhaseTimer {

	private String phaseId;
	private final int initialDuration;
	private int duration;
	private boolean actionBar = false;
	private Map<Integer, String> reminders = new HashMap<>();
	private Map<Integer, String> actionbarReminders = new HashMap<>();
	private boolean isRunning = false;

	private int task;

	public PhaseTimer(String phaseId, int duration) {
		this.phaseId = phaseId;
		this.initialDuration = duration;
		this.duration = duration;
	}

	/**
	 * Create a new {@link PhaseTimer}
	 *
	 * @param phase the phase id which have to be started at the end of timer
	 * @param duration the timer duration, in seconds
	 * @param actionBar say if the timer have to be displayed on actionbar
	 */
	public PhaseTimer(String phase, int duration, boolean actionBar) {
		this(phase, duration);

		this.actionBar = actionBar;
	}

	/**
	 * Add a recall at the given moment
	 * The placeholder %duration% is replaced by the current value
	 *
	 * @param when
	 *            le moment en secondes pr&eacute;c&eacute;dent le terme du timer
	 *            o&ugrave; il faut envoyer le message
	 * @param key
	 *            la cl&eacute; du message &agrave; envoyer dans l'action bar.
	 *            <p>
	 *            Le placeholder %duration% est remplac&eacute; par la dur&eacute;e
	 *            avant le terme du timer
	 *            <p>
	 *            Le placeholder %mins% est remplac�� par le nombre de minutes
	 *            restantes arrondit �� l'inf��rieur
	 *            <p>
	 *            Le placeholder %secs% est remplac�� par le nombre de secondes
	 *            restantes dans la minutes actuelle
	 */
	public void addReminder(int when, String key) {
		this.reminders.put(when, key);
	}

	/**
	 * Ajoute un rappel dans l'actionbar au moment donn&eacute;. Le placeholder
	 * %duration% est remplac&eacute; par le nombre de secondes actuelle
	 * 
	 * @param when
	 *            le moment en secondes pr&eacute;c&eacute;dent le terme du timer
	 *            o&ugrave; il faut envoyer le message
	 * @param key
	 *            la cl&eacute; du message &agrave; envoyer dans l'action bar.
	 *            <p>
	 *            Le placeholder %duration% est remplac&eacute; par le nombre de
	 *            secondes actuelle
	 *            <p>
	 *            Le placeholder %mins% est remplac�� par le nombre de minutes
	 *            restantes arrondit �� l'inf��rieur
	 *            <p>
	 *            Le placeholder %secs% est remplac�� par le nombre de secondes
	 *            restantes dans la minutes actuelle
	 */
	public void addActionbarReminder(int when, String key) {
		this.actionbarReminders.put(when, key);
	}

	/**
	 * Add a message which be showed when the timer will be between given value (included)
	 *
	 * @param from the min value (included)
	 * @param to the max value (included)
	 * @param key the message key to show
	 */
	public void addActionbarRange(int from, int to, String key) {
		for (int i = from; i >= to; i--) {
			if (!actionbarReminders.containsKey(i))
				actionbarReminders.put(i, key);
		}
	}

	/**
	 * Start the timer
	 */
	public void start() {
		this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(GameAPI.GAME_PROVIDER, () -> {
			if (actionBar && actionbarReminders.containsKey(duration)) {
				ActionBar.sendToAll(Messages.getMessage(
						actionbarReminders.get(duration).replaceAll("%secs%", String.valueOf(duration % 60))
								.replaceAll("%duration%", String.valueOf(duration))
								.replaceAll("%mins%", String.valueOf((int) Math.ceil(duration / 60))),
						"%secs%", String.valueOf(duration % 60)));
			}

			if (reminders.containsKey(duration)) {
				GameAPI.broadcast(
						reminders.get(duration).replaceAll("%secs%", String.valueOf(duration % 60))
								.replaceAll("%duration%", String.valueOf(duration))
								.replaceAll("%mins%", String.valueOf((int) Math.ceil(duration / 60))),
						"%secs%", String.valueOf(duration % 60));
				;
			}

			if (duration == 0) {
				GameAPI.startPhase(GameAPI.ACTIVE_GAME.phases.get(phaseId));
				this.cancel();
				return;
			}

			duration--;
		}, 0, 20);
		this.isRunning = true;
	}

	/**
	 * Cancel Timer
	 */
	public void cancel() {
		Bukkit.getScheduler().cancelTask(this.task);
		this.isRunning = false;
		duration = initialDuration;
	}

	public int getDuration() {
		return duration;
	}

	public int getInitialDuration() {
		return initialDuration;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public int getTask() {
		return this.task;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private String phaseId;
		private int duration;
		private boolean actionBar = false;
		private Map<Integer, String> reminders = new HashMap<>();
		private Map<Integer, String> actionbarReminders = new HashMap<>();

		/**
		 * Needed, define the phase to start at the end of the timer
		 * Prefer to use {@link #phase(String)} with the phase ID to prevent NPE
		 *
		 * @param phase the ID of the phase
		 * @return this builder
		 */
		@Deprecated
		public Builder phase(Phase phase) {
			this.phaseId = phase == null ? null : phase.id;
			return this;
		}

		/**
		 * Needed, define the phase to start at the end of the timer
		 *
		 * @param phaseId the ID of the phase
		 * @return this builder
		 */
		public Builder phase(String phaseId) {
			this.phaseId = phaseId;
			return this;
		}

		/**
		 * Needed, define the timer duration, in seconds
		 *
		 * @param duration the timer duration, in seconds
		 * @return this builder
		 */
		public Builder duration(int duration) {
			this.duration = duration;
			return this;
		}

		/**
		 * Optional, add a recall at the given moment
		 * <p>
		 * The placeholder %duration% is replaced by the current duration
		 * <p>
		 * The placeholder %mins% is replaced by the remaining minutes
		 * <p>
		 * The placeholder %secs% is replaced by the remaining seconds
		 *
		 * @param when the moment when the message have to be send
		 * @param key the message key to show
		 * @return this builder
		 */
		public Builder reminder(int when, String key) {
			this.reminders.put(when, key);
			return this;
		}

		/**
		 * Optional, add a recall at the given moment
		 * <p>
		 * The placeholder %duration% is replaced by the current duration
		 * <p>
		 * The placeholder %mins% is replaced by the remaining minutes
		 * <p>
		 * The placeholder %secs% is replaced by the remaining seconds
		 *
		 * @param when the moment when the message will be showed in actionbar
		 * @param key the message key to show
		 * @return this builder
		 */
		public Builder actionbarReminder(int when, String key) {
			this.actionbarReminders.put(when, key);
			return this;
		}

		/**
		 * Add a message which will be showed during two given values
		 *
		 * @param from the minimum value (included)
		 * @param to the maximum value (included)
		 * @param key the message key to show
		 * @return this builder
		 */
		public Builder actionbarRange(int from, int to, String key) {
			for (int i = from; i >= to; i--) {
				if (!actionbarReminders.containsKey(i))
					actionbarReminders.put(i, key);
			}

			return this;
		}

		/**
		 * Optional, say if the timer have to be showed in action bar
		 *
		 * @param actionBar true if the timer will be showed in action bar
		 * @return this builder
		 */
		public Builder actionbar(boolean actionBar) {
			this.actionBar = actionBar;
			return this;
		}

		/**
		 * Create the {@link PhaseTimer}
		 *
		 * @return a {@link PhaseTimer}
		 */
		public PhaseTimer build() {
			PhaseTimer phaseTimer = new PhaseTimer(this.phaseId, this.duration, this.actionBar);
			phaseTimer.reminders = reminders;
			phaseTimer.actionbarReminders = actionbarReminders;
			return phaseTimer;
		}

		/**
		 * Create the {@link PhaseTimer}
		 *
		 * @return a {@link PhaseTimer}
		 */
		public PhaseTimer start() {
			PhaseTimer timer = this.build();
			timer.start();
			return timer;
		}
	}
}
