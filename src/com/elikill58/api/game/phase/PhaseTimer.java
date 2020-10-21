package com.elikill58.api.game.phase;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

import com.elikill58.api.ActionBar;
import com.elikill58.api.Messages;
import com.elikill58.api.Timer;
import com.elikill58.api.game.GameAPI;

public class PhaseTimer {

	private Phase phase;
	private final int initialDuration;
	private int duration;
	private boolean actionBar = false;
	private Map<Integer, String> reminders = new HashMap<>();
	private Map<Integer, String> actionbarReminders = new HashMap<>();
	private boolean isRunning = false;

	private int task;

	public PhaseTimer(Phase phase, int duration) {
		this.phase = phase;
		this.initialDuration = duration;
		this.duration = duration;
	}

	/**
	 * Cr&eacute;e un nouveau {@link PhaseTimer}
	 *
	 * @param phase
	 *            la phase &agrave; d&eacute;marrer eu terme du timer
	 * @param duration
	 *            la dur&eacute;e du timer, en secondes
	 * @param actionBar
	 *            indique si le temps du timer doit &ecirc;tre indiqu&eacute; dans
	 *            l'action bar
	 */
	public PhaseTimer(Phase phase, int duration, boolean actionBar) {
		this(phase, duration);

		this.actionBar = actionBar;
	}

	/**
	 * Ajoute un rappel au moment donn&eacute;. Le placeholder %duration% est
	 * remplac&eacute; par la dur&eacute;e actuelle
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
	 * Démarre le timer
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
				GameAPI.startPhase(phase);
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

		private Phase phase;
		private int duration;
		private boolean actionBar = false;
		private Map<Integer, String> reminders = new HashMap<>();
		private Map<Integer, String> actionbarReminders = new HashMap<>();

		/**
		 * Obligatoire, d&eacute;finit la phase &agrave; d&eacute;marrer au terme du
		 * timer
		 *
		 * @param phase
		 *            la phae &agrave; d&eacute;marrer au terme du timer
		 * @return ce builder
		 */
		public Builder phase(Phase phase) {
			this.phase = phase;
			return this;
		}

		/**
		 * Obligatoire, d&eacute;finit la dur&eacute;e du timer, en secondes
		 *
		 * @param duration
		 *            la dur&eacute;e du timer, en secondes
		 * @return ce builder
		 */
		public Builder duration(int duration) {
			this.duration = duration;
			return this;
		}

		/**
		 * Optionnel, ajoute un rappel au moment donn&eacute;.
		 * <p>
		 * Le placeholder %duration% est remplac&eacute; par la dur&eacute;e actuelle
		 * <p>
		 * Le placeholder %mins% est remplac&eacute; par le nombre de minutes restantes
		 * <p>
		 * Le placeholder %secs% est remplac�� par le nombre de secondes restantes dans
		 * la minutes actuelle
		 *
		 * @param when
		 *            le moment pr&eacute;c&eacute;dent le terme du timer o&ugrave; il
		 *            faut envoyer le message
		 * @param key
		 *            la cl&eacute; du message &agrave; envoyer
		 * @return ce builder
		 */
		public Builder reminder(int when, String key) {
			this.reminders.put(when, key);
			return this;
		}

		/**
		 * Optionnel, ajoute un rappel au moment donn&eacute;.
		 * <p>
		 * Le placeholder %duration% est remplac&eacute; par la dur&eacute;e actuelle
		 * <p>
		 * Le placeholder %mins% est remplac&eacute; par le nombre de minutes restantes
		 * <p>
		 * Le placeholder %secs% est remplac�� par le nombre de secondes restantes dans
		 * la minutes actuelle
		 *
		 * @param when
		 *            le moment pr&eacute;c&eacute;dent le terme du timer o&ugrave; il
		 *            faut envoyer le message
		 * @param key
		 *            la cl&eacute; du message &agrave; envoyer
		 * @return ce builder
		 */
		public Builder actionbarReminder(int when, String key) {
			this.actionbarReminders.put(when, key);
			return this;
		}

		/**
		 * Ajoute un message qui sera affich&eacute; quand le timer sera entre les deux
		 * valeurs sp&eacute;cifi&eacute;es.
		 *
		 * @param from
		 *            la valeur minimale, inclus
		 * @param to
		 *            la valeur maximale, inclus
		 * @param key
		 *            la cl&eacute; du message &agrave; afficher
		 * @return ce builder
		 */
		public Builder actionbarRange(int from, int to, String key) {
			for (int i = from; i >= to; i--) {
				if (!actionbarReminders.containsKey(i))
					actionbarReminders.put(i, key);
			}

			return this;
		}

		/**
		 * Optionnel, indique si le temps du timer doit &ecirc;tre indiqu&eacute; dans
		 * l'action bar
		 *
		 * @param actionBar
		 *            true si le temps du timer doit &ecirc;tre indiqu&eacute; dans
		 *            l'action bar
		 * @return ce builder
		 */
		public Builder actionbar(boolean actionBar) {
			this.actionBar = actionBar;
			return this;
		}

		/**
		 * Cr&eacute;e le {@link PhaseTimer}
		 *
		 * @return le {@link PhaseTimer}
		 */
		public PhaseTimer build() {
			PhaseTimer phaseTimer = new PhaseTimer(this.phase, this.duration, this.actionBar);
			phaseTimer.reminders = reminders;
			phaseTimer.actionbarReminders = actionbarReminders;
			return phaseTimer;
		}

		/**
		 * Cr&eacute;e le {@link Timer} et le d&eacute;marre.
		 *
		 * @return le timer d&eacute;marr&eacute;
		 */
		public PhaseTimer start() {
			PhaseTimer timer = this.build();
			timer.start();
			return timer;
		}
	}
}
