package com.elikill58.api.game;

import javax.annotation.Nonnull;

import com.elikill58.api.data.Data;
import com.elikill58.api.game.phase.Phase;
import com.elikill58.api.game.phase.PhaseRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * It's a game which contains all default value and needed value
 */
public abstract class Game<T extends GameProperties> {

	/**
	 * All data to save
	 */
	public List<Data> dataValues = new ArrayList<>();
	
    /**
     * The game properties
     */
    @Nonnull
    public T properties = defaultProperties();

    /**
     * {@link PhaseRegistry} contains all phases of this game
     */
    @Nonnull
    public PhaseRegistry phases = new PhaseRegistry();

    /**
     * @return the game name without color
     */
    @Nonnull
    public abstract String name();
    
    /**
     * @return the game motd
     */
    @Nonnull
    public abstract String motd();

    /**
     * @return the game prefix
     */
    @Nonnull
    public abstract String prefix();

    /**
     * @return all developers of this game
     */
    @Nonnull
    public abstract List<String> developers();

    /**
     * @return the lobby phase during player wait the game start
     */
    @Nonnull
    public abstract Phase lobbyPhase();

    /**
     * @return the first phase of the game
     */
    @Nonnull
    public abstract Phase startingPhase();

    /**
     * @return the last phase before the end of the game
     */
    @Nonnull
    public abstract Phase endingPhase();

    /**
     * @return this {@link GameProperties} by default
     */
    @SuppressWarnings("unchecked")
    @Nonnull
    public T defaultProperties() {
        return (T) new GameProperties();
    }
}
