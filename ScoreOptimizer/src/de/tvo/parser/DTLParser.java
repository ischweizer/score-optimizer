package de.tvo.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import de.tvo.team.Competition;
import de.tvo.team.GEvent;
import de.tvo.team.TeamSheet;

public class DTLParser {
	public static DTLParser dtlParser = new DTLParser();
	
	private DTLParser() {
		
	}
	
	public TeamSheet readHomeTeam(String url) {
		return readResultsPage(url).getHome();
	}
	
	public TeamSheet readAwayTeam(String url) {
		return readResultsPage(url).getAway();
	}
	
	
	public Competition readResultsPage(String url) {
		final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);
	    HtmlPage page = null;
		try {
			page = webClient.getPage(url);
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	    final HtmlTableRow tr = (HtmlTableRow) page.getByXPath("//tr[@class='noHG']").get(1);
	    List<HtmlTableCell> tcs = tr.getCells();
	    
	    TeamSheet home = new TeamSheet(tcs.get(0).asText());
	    TeamSheet away = new TeamSheet(tcs.get(1).asText());
	    
	    HtmlTable ht = page.getHtmlElementById("ColorTable");
	    for(int i = 0; i < 6; i++) {
	    	String[] namesHome = new String[4];
	    	String[] namesAway = new String[4];
	    	
	    	double[] resultsHome = new double[4];
	    	double[] resultsAway = new double[4];
	    	
	    	double[] difficultiesHome = new double[4];
	    	double[] difficultiesAway = new double[4];
	    	
	    	int[] scoresHome = new int[4];
	    	int[] scoresAway = new int[4];
	    	
	    	for(int j = 0; j < 4; j++) {
	    		int rowID = ((i*7)+(j+3));
	    		
	    		HtmlTableCell hn = ht.getCellAt(rowID, 0);
	    		String name = hn.asText().split(" - ")[0];
	    		namesHome[j] = name;
	    		
	    		hn = ht.getCellAt(rowID, 1);
	    		double difficulty = Double.parseDouble(hn.asText().replace(',', '.'));
	    		difficultiesHome[j] = difficulty;
	    		
	    		hn = ht.getCellAt(rowID, 2);
	    		double result = Double.parseDouble(hn.asText().replace(',', '.'));
	    		resultsHome[j] = result;
	    		
	    		hn = ht.getCellAt(rowID, 3);
	    		int score = Integer.parseInt(hn.asText());
	    		scoresHome[j] = score;
	    		
	    		hn = ht.getCellAt(rowID, 5);
	    		name = hn.asText().split(" - ")[0];
	    		namesAway[j] = name;
	    		
	    		hn = ht.getCellAt(rowID, 6);
	    		difficulty = Double.parseDouble(hn.asText().replace(',', '.'));
	    		difficultiesAway[j] = difficulty;
	    		
	    		hn = ht.getCellAt(rowID, 7);
	    		result = Double.parseDouble(hn.asText().replace(',', '.'));
	    		resultsAway[j] = result;
	    		
	    		hn = ht.getCellAt(rowID, 8);
	    		score = Integer.parseInt(hn.asText());
	    		scoresAway[j] = score;
	    		
	    	}
	    	GEvent gHome = new GEvent(resultsHome, difficultiesHome, namesHome, scoresHome);
	    	home.addEvent(gHome);
	    	GEvent gAway = new GEvent(resultsAway, difficultiesAway, namesAway, scoresAway);
	    	away.addEvent(gAway);
	    	
	    }
	    webClient.closeAllWindows();
	    
		return new Competition(home, away);
		
	}
}
