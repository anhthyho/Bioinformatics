import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/*
 * reference lecture slide for trees: http://www.bioinfo.rpi.edu/bystrc/courses/biol4540/lecture.pdf
 */
public class DistanceCalculator {
	private String[] taxa;
	private String[] names;
	private ArrayList<String> list;
	private ArrayList<String> listNames;
	double[][] original;
	private String remX;
	private String remY;

	private HashMap<String, Integer> map;

	public DistanceCalculator(String[] taxa, String[] names) {
		this.taxa = taxa;
		this.names = names;
		list = new ArrayList<String>(Arrays.asList(taxa));
		listNames = new ArrayList<String>(Arrays.asList(names));
		map = new HashMap<>();
		for (int i = 0; i < listNames.size(); i++) {
			map.put(listNames.get(i), i);
		}

	}

	public DistanceCalculator(String[] names) {
		this.names = names;
		listNames = new ArrayList<String>(Arrays.asList(names));
		map = new HashMap<>();
		for (int i = 0; i < listNames.size(); i++) {
			map.put(listNames.get(i), i);
		}
	}

	public double calcDistance(String a, String b) {
		double distance;
		if (a.length() != b.length()) {
			System.out.println("diff length");
			distance = 0;
		} else {
			double differences = 0;
			double l = a.length();
			for (int i = 0; i < l; i++) {
				if (!a.substring(i, i + 1).equals(b.substring(i, i + 1))) {
					differences++;
				}
			}
			distance = differences / l;
		}
		return distance * 100;
	}

	public double calcJukesCantor(String a, String b) {
		double distance = calcDistance(a, b);
		double d = (4.0 / 3) * distance;
		double JCDistance = -0.75 * Math.log(1 - d);
		return JCDistance;
	}

	public double[][] distanceMatrix() {
		double[][] matrix = new double[list.size()][listNames.size()];
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < listNames.size(); j++) {
				if (i == j) {
					matrix[i][j] = -1;
				} else {
					matrix[j][i] = calcDistance(list.get(i), list.get(j));
					matrix[i][j] = -1;
				}
			}
		}
		return matrix;
	}

	public double[][] newMatrix(double[][] original) {
		double[][] matrix = new double[listNames.size()][listNames.size()];
		for (int i = 0; i < listNames.size(); i++) {
			for (int j = 0; j < listNames.size(); j++) {

				if (i == j) {
					matrix[i][j] = -1;
				} else if (j == 0) {
					// System.out.println("j: " + listNames.get(j));
					// System.out.println("i: " + listNames.get(i));
					// System.out.println("remX: " +remX + " remY: " + remY + " i: " + i);
					// System.out.println(map.get(listNames.get(i)));
					// System.out.println(map.get(remX));

					double first = 0;
					double second = 0;
					if (original[map.get(listNames.get(i))][map.get(remX)] == -1) {
						first = (original[map.get(remX)][map.get(listNames.get(i))]);
						second = (original[map.get(remY)][map.get(listNames.get(i))]);
					} else {
						first = (original[map.get(listNames.get(i))][map.get(remX)]);
						second = (original[map.get(listNames.get(i))][map.get(remY)]);
					}

					matrix[j][i] = (first + second) / 2;
					matrix[i][j] = -1;
				} else {
					int a = map.get(listNames.get(i));
					int b = map.get(listNames.get(j));
					matrix[j][i] = original[b][a];
					matrix[i][j] = -1;
				}

			}
		}
		return matrix;

	}

	public String findShortest(double[][] matrix) {
		String matches = "";
		String posX = listNames.get(0);
		String posY = listNames.get(1);
		int x = 0;
		int y = 1;
		double min = matrix[0][1];
		// System.out.println("first min: " + min);
		for (int i = 0; i < matrix.length; i++) {
			for (int j = i + 1; j < matrix.length - 1; j++) {
				double val = matrix[i][j];
				if (val != -1 && val < min) {
					min = val;
					// System.out.println("i and j: " + i + " " + j);
					x = i;
					y = j;
					posX = listNames.get(i);
					posY = listNames.get(j);
					// System.out.println(posX + " " + posY);
				}
			}
		}
		matches = "Match: " + listNames.get(x) + "," + listNames.get(y) + " | Distance: " + String.format(" %.1f ", min)
				+ "\n";
		removeShortest(x, y, posX, posY);
		return matches;

	}

	public void removeShortest(int x, int y, String a, String b) {
		// System.out.println(x + " " + y );
		if (x < y) {
			y = y - 1;
		}
		// list.remove(x);
		// list.remove(y);
		listNames.remove(x);
		listNames.remove(y);

		remX = a;
		remY = b;

		System.out.println(a + " and " + b);

		// map.remove(a);
		// map.remove(b);

		String comb = a + "" + b;
		listNames.add(0, comb);
		// list.add(0, comb);
		map.put(comb, listNames.size());

	}

	public static void print(HashMap<String, Integer> map) {
		if (map.isEmpty()) {
			System.out.println("map is empty");
		}

		else {
			System.out.println(map);
		}
	}

	public void performCalc(double[][] mat) {
		System.out.println("-----------------------------------------------");
		if (mat.length > 1) {
			map.clear();
			for (int i = 0; i < listNames.size(); i++) {
				map.put(listNames.get(i), i);
			}
			// print to check correct indices
			// print(map);
			printMatrix(mat);
			String matches = findShortest(mat);
			System.out.println(matches);
			performCalc(newMatrix(mat));
		}
	}

	public void printMatrix(double[][] mat) {
		System.out.print("    ");
		for (String s : listNames) {
			String f = String.format("%1$" + 6 + "s", s);
			System.out.print(f);
		}
		System.out.println();
		for (int i = 0; i < mat.length; i++) {
			System.out.print(String.format("%1$" + 4 + "s", listNames.get(i)) + "  ");
			for (int j = 0; j < mat[0].length; j++) {
				if (mat[i][j] < 0) {
					System.out.print("----- ");
				} else {
					String s = String.format("%.1f ", mat[i][j]);
					System.out.print(s + " ");
				}

			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		String[] taxa = { "ATGGCTATTCTTATAGTACG", "ATCGCTAGTCTTATATTACA", "TTCACTAGACCTGTGGTCCA",
				"TTGACCAGACCTGTGGTCCG", "TTGACCAGTTCTCTAGTTCG" };
		String[] names = { "A", "B", "C", "D", "E" };

		DistanceCalculator dc = new DistanceCalculator(taxa, names);
		double[][] mat = dc.distanceMatrix();
		dc.performCalc(mat);

		double[] a = { -1, 20, 50, 45, 40 };
		double[] b = { -1, -1, 40, 55, 50 };
		double[] c = { -1, -1, -1, 15, 40 };
		double[] d = { -1, -1, -1, -1, 25 };
		double[] e = { -1, -1, -1, -1, -1 };

		double[][] test = { a, b, c, d, e };
		DistanceCalculator dctest = new DistanceCalculator(names);
		dctest.performCalc(test);

	}

}
