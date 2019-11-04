public class DistanceCalculator {
	private String[] taxa;
	private String[] names; 
	public DistanceCalculator(String[] taxa, String[] names){
		this.taxa = taxa; 
		this.names = names; 
	}
	
	public double calcDistance(String a, String b) {
		double distance; 
		if (a.length() != b.length()) {
			System.out.println("diff length");
			distance = 0;
		}
		else {
			double differences = 0; 
			double l = a.length();
			for (int i=0; i<l; i++) {
				if (!a.substring(i,i+1).equals(b.substring(i,i+1))) {
					differences++;
				}
			}
			distance = differences/l;
		} 
		return distance;
	}
	
	public double calcJukesCantor(String a, String b) {
		double distance = calcDistance(a,b); 
		double d = (4.0/3)* distance; 
		double JCDistance = -0.75 * Math.log(1-d);
		return JCDistance;
	}
	
	public double[][] distanceMatrix(){
		double[][] matrix = new double[taxa.length][taxa.length]; 
		for (int i =0; i<taxa.length; i++) {
			for (int j=0; j<taxa.length; j++) {
				if (i==j) {
					matrix[i][j] = -1;
				}
				else {
					matrix[j][i] = calcDistance(taxa[i], taxa[j]);
					matrix[i][j] = calcJukesCantor(taxa[i], taxa[j]);
				}
			}
		}
		return matrix; 
	}
	
	public void printMatrix(double[][] mat) {
		System.out.print("     ");
		for (String s: names) {
			System.out.print( s + "    ");
		}
		System.out.println(); 
		for (int i = 0; i<mat.length; i++) {
			System.out.print (names[i] + "  ");
			for (int j =0; j<mat[0].length; j++) {
				if (mat[i][j] < 0) {
					System.out.print("---- ");
				}
				else {
					String s = String.format("%.2f", mat[i][j]);
					System.out.print(s + " ");
				}
				
			}
			System.out.println(); 
		}
	}
	
	public static void main(String[]args) {
		String[] taxa = {"ATGGCTATTCTTATAGTACG", "ATCGCTAGTCTTATATTACA", "TTCACTAGACCTGTGGTCCA", "TTGACCAGACCTGTGGTCCG", "TTGACCAGTTCTCTAGTTCG"}; 
		String[] names = {"A", "B", "C", "D", "E"}; 
		DistanceCalculator dc = new DistanceCalculator(taxa, names); 
		double[][] mat = dc.distanceMatrix();
		dc.printMatrix(mat); 
		

	}

}
