package pl.mawe.games.commons;

public enum GameGlobalState {

	INSTANCE;

	private GameGlobalState() {
	}

	private GameState gameState;

	public void setState(GameState gameState) {
		this.gameState = gameState;
	}

	public GameState getState() {
		return gameState;
	}

}
