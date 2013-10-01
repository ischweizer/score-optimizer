package de.tvo.team;

import java.util.ArrayList;

public class TeamSheet {
	private ArrayList<GEvent> gevents = new ArrayList<GEvent>();
	private String name;
	
	public String getName() {
		return name;
	}

	public ArrayList<GEvent> getGevents() {
		return gevents;
	}

	public TeamSheet(String name) {
		this.name = name;
	}
	
	public void addEvent(GEvent g) {
		gevents.add(g);
	}
	
	public GEvent getEvent(int i) {
		return gevents.get(i);
	}
	
	public double[][] getResultArray() {
		double[][] d = new double[6][4];
		for(int i = 0; i < gevents.size(); i++) {
			d[i] = gevents.get(i).getResults();
		}
		return d;
	}
	
	public double[][] getDfficultiesArray10() {
		double[][] d = new double[6][4];
		for(int i = 0; i < gevents.size(); i++) {
			double[] di = gevents.get(i).getDifficulties();
			for(int j = 0; j < di.length; j++)
			d[i][j] = di[j] + 10.0;
		}
		return d;
	}

	public int getScore() {
		int sum = 0;
		for(GEvent g: gevents) 
		{
			sum += g.getScore();
		}
			return sum;
	}

	public String[][] getNames() {
		String[][] s = new String[6][4];
		for(int i = 0; i < gevents.size(); i++) {
			s[i] = gevents.get(i).getNames();
		}
		return s;
	}

}
