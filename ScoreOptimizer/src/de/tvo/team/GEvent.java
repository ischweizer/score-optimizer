package de.tvo.team;

public class GEvent {
	private double[] results = new double[4];
	private double[] difficulties = new double[4];
	private String[] names = new String[4];
	private int[] scores = new int[4];
	
	public GEvent(double[] results, double[] difficulties, String[] names, int[] scores) {
		if(results.length==4) this.results = results;
		if(difficulties.length==4) this.difficulties = difficulties;
		if(names.length==4) this.names = names;
		if(scores.length == 4) this.scores = scores;
	}

	public double[] getResults() {
		return results;
	}

	public double[] getDifficulties() {
		return difficulties;
	}

	public String[] getNames() {
		return names;
	}

	public int[] getScores() {
		return scores;
	} 
	
	public double getPoints() {
		double sum = 0.0;
		for(int i = 0; i < results.length; i++) {
			sum+=results[i];
		}
		return sum;
	}
	
	public double getDifficultiesSum() {
		double sum = 0.0;
		for(int i = 0; i < results.length; i++) {
			sum+=results[i]+10;
		}
		return sum;
	}
	
	public double getPercentage() {
		return 100 * (getPoints() / getDifficultiesSum());
	}
	
	public int getScore() {
		int sum = 0;
		for(int i = 0; i < scores.length; i++) {
			sum+= scores[i];
		}
		return sum;
	}
}
