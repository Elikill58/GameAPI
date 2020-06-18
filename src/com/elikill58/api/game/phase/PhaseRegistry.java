package com.elikill58.api.game.phase;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class PhaseRegistry {

    private Map<String, Phase> phases = new HashMap<>();

    /**
     * Register a new {@link Phase}.
     *
     * @param phase the phase that must be registered
     */
    public void register(Phase phase) {
        this.phases.put(phase.id, phase);
    }

    /**
     * Remove a {@link Phase}
     * @param name the name of the phase to remove
     */
    public void unregister(String name) {
        this.phases.remove(name);
    }

    /**
     * Return the phase get by name
     *
     * @param phase the phase name
     * @return the phase
     */
    public Phase get(String phase) {
        return this.phases.get(phase);
    }

    /**
     * Run action on each phase
     * @param action the action that phases must run
     */
    public void forEach(Consumer<Phase> action) {
        this.phases.values().forEach(action);
    }

    /**
     * Return all registered phases
     *
     * @return all phases currently registered
     */
    public Collection<Phase> getRegisteredPhases() {
        return this.phases.values();
    }

    /**
     * Return all registered phases name
     *
     * @return All phases named
     */
    public Collection<String> getRegisteredPhasesNames() {
        return this.phases.keySet();
    }
}
