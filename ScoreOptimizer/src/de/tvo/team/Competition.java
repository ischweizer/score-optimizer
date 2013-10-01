package de.tvo.team;

public class Competition {
	private TeamSheet home;
	private TeamSheet away;
	
	public Competition(TeamSheet home, TeamSheet away)  {
		this.home = home;
		this.away = away;
	}

	public TeamSheet getHome() {
		return home;
	}

	public TeamSheet getAway() {
		return away;
	}
}
