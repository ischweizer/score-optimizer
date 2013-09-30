package de.tvo.main;

import java.io.ObjectInputStream.GetField;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

/**
 * Copyright (C) 2013 Immanuel Schweizer
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */
public class ScoreOptimizer {
	private int home_total_max = 0;
	private int away_total_max = 0;
	private int home_total_min = 0;
	private int away_total_min = 0;

	String[] names = { "Boden", "Pauschenpferd", "Ringe", "Sprung", "Barren",
			"Reck" };

	public ScoreOptimizer(double[][] a, double[][] b, double[][] a_a,
			double[][] b_a, String h_name, String a_name, int h, int aw, boolean details, boolean isAway) {
		System.out.println("==================================");
		if(!isAway) {
			System.out.println("Wettkampf: " + h_name + " gegen " + a_name);
		}
		else {
			System.out.println("Wettkampf: " + a_name + " gegen " + h_name);
		}
		for (int i = 0; i < 6; i++) {
			ArrayList<int[]> result = optimize(a[i], b[i]);
			int[] results = result.get(0);
			int[] combines = result.get(1);
			int[] points = result.get(2);
			int home = results[1];
			int away = results[2];
			home_total_max += home;
			away_total_max += away;
			if (details) {
				System.out.println(names[i]);
				System.out.println("Optimum");
				System.out.println("Paarungen:");
				for (int j = 0; j < 4; j++) {
					System.out.println("(" + a[i][j] + ") " + j + " - "
							+ combines[j] + " (" + b[i][combines[j]] + ") ["
							+ points[j] + "]");
				}
				System.out.println("Ergebnis: " + home + ":" + away);
			}
			result = optimize(b[i], a[i]);
			results = result.get(0);

			home = results[2];
			away = results[1];

			home_total_min += home;
			away_total_min += away;
			if (details) {
				System.out.print("Worst-Case: ");
				System.out.println("Ergebnis: " + home + ":" + away);
			}
		}
		double total_h = 0;
		double total_h_a = 0;
		double total_a = 0;
		double total_a_a = 0;
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 4; j++) {
				total_h += a[i][j];
				total_a += b[i][j];
				total_h_a += a_a[i][j];
				total_a_a += b_a[i][j];
			}
		}
		double per_h = 100 * total_h / total_h_a;
		double per_a = 100 * total_a / total_a_a;
		double average_h = (total_h_a / 24.0) - (total_h / 24.0);
		double average_a = (total_a_a / 24.0) - (total_a / 24.0);
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.HALF_UP);
		df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
		System.out.println("Optimum: Home: " + home_total_max + " Away: "
				+ away_total_max);
		System.out.println("Worst-Case: Home: " + home_total_min + " Away: "
				+ away_total_min);
		System.out.print("Real: ");
		System.out.println("Home: " + h + " (" + df.format(total_h) + " ["
				+ df.format(total_h_a) + ", " + df.format(per_h) + "%, "
				+ df.format(average_h) + "]) Away: " + aw + " ("
				+ df.format(total_a) + " [" + df.format(total_a_a) + ", "
				+ df.format(per_a) + "%, " + df.format(average_a) + "])");
		System.out.println("==================================");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		double[][] a = { { 11.75, 11.75, 11.65, 12.65 },
				{ 9.8, 9.35, 11.25, 10.8 }, { 10.3, 11.3, 13.1, 10.65 },
				{ 12.75, 11.6, 10.75, 11.80 }, { 11.65, 11.75, 12.10, 10.6 },
				{ 11.25, 11.05, 11.6, 11.0 } };
		double[][] a_a = { { 13.5, 12.8, 13.3, 13.8 },
				{ 13.2, 13.0, 13.2, 13.2 }, { 12.4, 13.2, 14.7, 12.4 },
				{ 13.6, 12.8, 12.0, 13.0 }, { 13.1, 13.2, 13.2, 13.1 },
				{ 12.9, 12.5, 12.9, 12.9 } };
		double[][] b = { { 12.95, 10.55, 13.20, 12.50 },
				{ 10.90, 11.05, 11.35, 10.70 }, { 11.85, 12.00, 11.05, 10.25 },
				{ 12.6, 12.9, 12.5, 11.45 }, { 12.3, 11.75, 12.65, 12.20 },
				{ 8.2, 9.4, 12.05, 9.7 } };
		double[][] b_a = { { 14.1, 13.5, 14.2, 14.4 },
				{ 13.4, 13.0, 13.0, 12.8 }, { 13.6, 13.5, 13.0, 11.9 },
				{ 13.6, 13.6, 13.6, 13.6 }, { 13.4, 13.1, 13.8, 13.3 },
				{ 12.4, 12.1, 13.3, 12.4 } };
		new ScoreOptimizer(a, b, a_a, b_a, "TV 1877 Ober Ramstadt", "KTV Hohenlohe", 28, 43, true, false);

		double[][] c = { { 11.75, 11.75, 11.65, 12.65 },
				{ 9.8, 9.35, 11.25, 10.8 }, { 10.3, 11.3, 13.1, 10.65 },
				{ 12.75, 11.6, 10.75, 11.80 }, { 11.65, 11.75, 12.10, 10.6 },
				{ 11.25, 11.05, 11.6, 11.0 } };
		double[][] c_a = { { 13.5, 12.8, 13.3, 13.8 },
				{ 13.2, 13.0, 13.2, 13.2 }, { 12.4, 13.2, 14.7, 12.4 },
				{ 13.6, 12.8, 12.0, 13.0 }, { 13.1, 13.2, 13.2, 13.1 },
				{ 12.9, 12.5, 12.9, 12.9 } };
		double[][] d = { { 10.65, 11.85, 12.25, 12.60 },
				{ 11.1, 11.35, 7.0, 2.15 }, { 11.05, 9.85, 10.85, 11.35 },
				{ 12.55, 11.65, 11.80, 12.30 }, { 10.45, 11.70, 11.15, 11.20 },
				{ 10.3, 9.6, 9.95, 11.9 } };
		double[][] d_a = { { 13.5, 14.0, 13.9, 13.8 },
				{ 13.1, 13.3, 12.8, 12.1 }, { 13.7, 12.5, 14.4, 14.0 },
				{ 13.6, 13.0, 13.6, 13.6 }, { 13.0, 13.0, 13.3, 13.7 },
				{ 12.5, 12.5, 13.0, 13.2 } };
		ScoreOptimizer sO = new ScoreOptimizer(c, d, c_a, d_a, "TV 1877 Ober Ramstadt", "TG Wangen / Eisenharz", 0, 0, true, true);
		try {
			sO.getResultsForURL();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
/*		double[][] c = { { 14.60, 14.00, 14.55, 12.85 },
				{ 13.7, 13.1, 15.15, 10.90 }, { 14.4, 11.75, 12.5, 14.5 },
				{ 12.95, 14.5, 14.35, 12.85 }, { 14.8, 14.15, 11.95, 12.75 },
				{ 12.45, 14.8, 11.1, 15.45 } };
		double[][] c_a = { { 13.5, 12.8, 13.3, 13.8 },
				{ 13.2, 13.0, 13.2, 13.2 }, { 12.4, 13.2, 14.7, 12.4 },
				{ 13.6, 12.8, 12.0, 13.0 }, { 13.1, 13.2, 13.2, 13.1 },
				{ 12.9, 12.5, 12.9, 12.9 } };
		double[][] d = { { 13.25, 14.4, 14.3, 13.4 },
				{ 13.05, 12.3, 13.7, 12.75 }, { 14.5, 13.75, 14.1, 14.0},
				{ 13.05, 13.65, 14.05, 13.20 }, { 13.6, 13.3, 13.95, 13.15 },
				{ 14.95, 14.5, 12.55, 13.6 } };
		double[][] d_a = { { 13.5, 14.0, 13.9, 13.8 },
				{ 13.1, 13.3, 12.8, 12.1 }, { 13.7, 12.5, 14.4, 14.0 },
				{ 13.6, 13.0, 13.6, 13.6 }, { 13.0, 13.0, 13.3, 13.7 },
				{ 12.5, 12.5, 13.0, 13.2 } };
		new ScoreOptimizer(c, d, c_a, d_a, "KTV Obere Lahn", "KTV Straubenhardt", 0, 0, true, true);*/

	}

	/**
	 * @param a
	 * @param b
	 * @return
	 */
	public ArrayList<int[]> optimize(double[] a, double[] b) {
		int[] results = new int[3];
		boolean s_u = false;
		int[] best_combine = new int[4];
		int[] points = new int[4];
		int max_points = -50;
		int home = 0;
		int away = 0;
		for (int i = 0; i < 4; i++) {
			for (int k = i + 1; k < 3 + i + 1; k++) {
				for (int s = k + 1; s < 2 + k + 1; s++) {
					int sum_points = 0;
					int k_m = k % 4;
					int s_m = s % 4;
					if (s_m == (i + 1) % 4 && s_u) {
						s_m = (s_m + 1) % 4;
						s_u = false;
					}
					if (s_m == i) {
						s_m = (s_m + 1) % 4;
						s_u = true;
					}
					int[] combines = new int[4];
					int ho = 0;
					int aw = 0;
					int[] points_lokal = new int[4];

					for (int j = 0; j < 4; j++) {

						if (k_m == i)
							k_m = (k_m + 1) % 4;

						combines[0] = i;
						combines[1] = k_m;
						combines[2] = s_m;
						combines[3] = 6 - i - k_m - s_m;
						double a_c = a[j];
						double b_c = b[combines[j]];

						double dif = a_c - b_c;
						double dif_a = Math.abs(dif);
						int point = 0;
						if (dif_a <= 0.1)
							point = 0;
						else if (dif_a <= 0.25)
							point = 1;
						else if (dif_a <= 0.5)
							point = 2;
						else if (dif_a <= 1.0)
							point = 3;
						else if (dif_a <= 2.0)
							point = 4;
						else if (dif_a <= 6.0)
							point = 5;
						else
							point = 10;

						if (dif > 0) {
							sum_points += point;
							ho += point;
							points_lokal[j] = point;
						} else {
							sum_points -= point;
							aw += point;
							points_lokal[j] = -point;
						}
					}
					if (sum_points > max_points || max_points == -50) {
						max_points = sum_points;
						best_combine = combines;
						home = ho;
						away = aw;
						points = points_lokal;
					}
				}
			}
		}
		results[0] = max_points;
		results[1] = home;
		results[2] = away;
		ArrayList<int[]> result = new ArrayList<int[]>();
		result.add(results);
		result.add(best_combine);
		result.add(points);

		return result;

	}
	
	public void getResultsForURL() throws Exception {
		final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);
	    final HtmlPage page = webClient.getPage("http://www.deutsche-turnliga.de/maenner/regionalliga_s/detail.php?ID=1010");
	    final List<?> divs = page.getByXPath("//div");

	    //get div which has a 'name' attribute of 'John'
	    final HtmlTableRow tr = (HtmlTableRow) page.getByXPath("//tr[@class='noHG']").get(1);
	    List<HtmlTableCell> tcs = tr.getCells();
	    for(HtmlTableCell tc:tcs) {
	    	System.out.println(tc.asText());
	    }
	    
	    HtmlTable ht = page.getHtmlElementById("ColorTable");
	    for(int i = 0; i < 6; i++) {
	    	for(int j = 0; j < 4; j++) {
	    		HtmlTableCell hn = ht.getCellAt(((i*7)+(j+3)), 0);
	    		String name = hn.asText().split(" - ")[0];
	    		System.out.print(name + " - ");
	    		
	    		hn = ht.getCellAt(((i*7)+(j+3)), 5);
	    		name = hn.asText().split(" - ")[0];
	    		System.out.println(name);
	    	}
	    }
//	    System.out.println(ht.asXml());
//	    String names = tr.asText();
	    


	    webClient.closeAllWindows();
	}
}
