
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.io.*;
import javax.swing.*;

/**
 * calc panel is the gui implementation of distance calculator
 * this panel allows users to enter their own values and see the resulting distance matrices
 * @author anhthy ho// december 2019 for cs123a bioinformatics at sjsu
 *
 */
public class CalcPanel extends JPanel {
	/**
	 * calc panel constructor includes taxa entry panel, distance entry panel and result panel
	 */
	public CalcPanel() {
		this.setName("Distance Calculator");
		this.setSize(1500, 1500);
		// this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		/*
		 * start creation of panel components for taxa panel
		 * includes name and input box, buttons to add and calculate distance 
		 */
		JButton taxa = new JButton("Add Taxa Entry");
		JTextField name = new JTextField("Enter name");
		JTextField input = new JTextField("Enter taxa");

		ArrayList<String> tArray = new ArrayList<>();
		ArrayList<String> nArray = new ArrayList<>();

		JTextArea res = new JTextArea(40, 20);
		JScrollPane sp = new JScrollPane(res);
		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		PrintStream out = new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				res.append("" + (char) (b & 0xFF));
			}
		});
		System.setOut(out);

		JPanel taxaEntry = new JPanel();
		taxaEntry.setLayout(new BoxLayout(taxaEntry, BoxLayout.Y_AXIS));

		Color taxaColor = Color.decode("#D0DEBB");
		taxaEntry.setBackground(taxaColor);

		JTextArea descTaxa = new JTextArea(5, 25);
		descTaxa.setText("Enter name and taxa then click 'Add Taxa Entry'" + "\n"
				+ "to add to list. Once done with taxa and name additions, \n"
				+ "click 'Calculate Taxa Entry' to display matches and distances.");
		descTaxa.setBackground(taxaColor);
		taxaEntry.add(descTaxa);

		
		
		JTextArea list = new JTextArea(5, 20);
		Color gray = Color.decode("#BBBEAE");
		JScrollPane sp2 = new JScrollPane(list);
		sp2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		list.setBackground(gray);
		JLabel lDist = new JLabel("Name and Taxa");
		// taxaEntry.add(list);

		JLabel nameP = new JLabel("Add Name");
		JLabel inputPrompt = new JLabel("Add Taxa");
		JPanel taxaInput = new JPanel();
		taxaInput.setLayout(new BoxLayout(taxaInput, BoxLayout.Y_AXIS));
		taxaInput.add(nameP);
		taxaInput.add(name);
		taxaInput.add(inputPrompt);
		taxaInput.add(input);
		taxaInput.add(taxa);
		taxaInput.add(lDist);
		taxaInput.add(sp2);

		taxaInput.setBackground(taxaColor);

		taxaEntry.add(taxaInput);

		/*
		 * taxa button will add the inputs to lists and print in the added section 
		 */
		taxa.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String i = input.getText();
				String j = name.getText();

				tArray.add(i);
				nArray.add(j);
				list.append("\n" +"name: " + j + " taxa: " + i);

				input.setText("");
				name.setText("");
			}

		});

		/**
		 * calc taxa using inputs
		 * only for taxa entry
		 */
		JButton calculateTaxa = new JButton("Calculate Taxa Entry");
		calculateTaxa.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String[] t = new String[tArray.size()];
				for (int i = 0; i < tArray.size(); i++)
					t[i] = tArray.get(i);

				String[] n = new String[nArray.size()];
				for (int i = 0; i < nArray.size(); i++)
					n[i] = nArray.get(i);

				DistanceCalculator dc = new DistanceCalculator(t, n);
				double[][] mat = dc.distanceMatrix();
				dc.performCalc(mat);
				
				tArray.clear();
				nArray.clear();
				res.append(list.getText());
				list.setText("");
				
				res.append("\n");
				ArrayList<String> branches = dc.getBranches(); 
				for (String s: branches) {
					res.append(s);
				}
				
			}

		});
		
		JButton clear = new JButton("Clear List");
		clear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (tArray.size()>=1){
					tArray.clear();
					nArray.clear();
					list.setText("");
				}
				else {
					list.setText("There are no items, cannot clear.");
				}
			}

		});
		
		JButton removeLatest = new JButton("Remove Last Entry");
		removeLatest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (tArray.size()>1) {
					tArray.remove(tArray.size()-1);
					nArray.remove(tArray.size()-1);
					list.setText(list.getText().substring(0,list.getText().lastIndexOf("\n")));
				}
				else if (tArray.size()==1) {
					list.setText("Cannot remove as there is only one item, please clear the list");
				}
				else {
					list.setText("The list is empty, nothing to remove");
				}
			}

		});

		JPanel taxaButtons = new JPanel(); 
		taxaButtons.setBackground(taxaColor);
		taxaButtons.add(clear);
		taxaButtons.add(removeLatest);
		taxaButtons.add(calculateTaxa);
		taxaEntry.add(taxaButtons);

		/*
		 * begins creation of distance entry panel
		 * includes name and distance box, buttons to add and calculate distance 
		 */
		JButton distances = new JButton("Add Distance Entry");
		JTextField dName = new JTextField("");
		JTextField dDistances = new JTextField("");

		ArrayList<String> dNArray = new ArrayList<>();
		ArrayList<double[]> dDArray = new ArrayList<>();

		JPanel distEntry = new JPanel();
		distEntry.setLayout(new BoxLayout(distEntry, BoxLayout.Y_AXIS));

		JTextArea descDist = new JTextArea(5, 25);

		descDist.setText("Enter name and distances as comma separated list then click 'Add Distance Entry'" + "\n"
				+ "to add to list. Once done with distance and name additions, \n"
				+ "click 'Calculate Distance Entry' to display matches and distances. \nNote: for no value, enter -1");
		distEntry.add(descDist);

		JTextArea dList = new JTextArea(5, 20);
		JLabel ndDist = new JLabel("Name and Distances");
		// taxaEntry.add(list);
		
		JScrollPane sp3 = new JScrollPane(dList);
		sp3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		JLabel namePrompt = new JLabel("Add Name");
		JLabel distancePrompt = new JLabel("Add Distances");

		JPanel distInput = new JPanel();
		distInput.setLayout(new BoxLayout(distInput, BoxLayout.Y_AXIS));
		distInput.add(namePrompt);
		distInput.add(dName);
		distInput.add(distancePrompt);
		distInput.add(dDistances);
		distInput.add(distances);
		distInput.add(ndDist);
		distInput.add(sp3);

		distEntry.add(distInput);

		/*
		 * creates array to add distance into and names
		 */
		distances.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String j = dName.getText();
				String i = dDistances.getText();

				String[] sp = i.split(",");
				double[] db = new double[sp.length];

				for (int k = 0; k < db.length; k++)
					db[k] = Double.parseDouble(sp[k]);
				dDArray.add(db);
				dNArray.add(j);
				dList.append("\n" + "name: " + j + " distances: " + i);

				dName.setText("");
				dDistances.setText("");
			}

		});

		/*
		 * calls on distancecalculator to perform cal
		 * creates double matrix 
		 */
		JButton calculateDistances = new JButton("Calculate Distance Entry");
		calculateDistances.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				double[][] distances = new double[dDArray.size()][dDArray.size()];
				for (int k = 0; k < distances.length; k++)
					distances[k] = dDArray.get(k);

				String[] dNames = new String[dNArray.size()];
				for (int k = 0; k < dNArray.size(); k++)
					dNames[k] = dNArray.get(k);

				DistanceCalculator dctest = new DistanceCalculator(dNames);
				dctest.performCalc(distances);
				
				dDArray.clear();
				dNArray.clear();
				res.append(dList.getText());
				dList.setText("");
				
				res.append("\n");
				ArrayList<String> branches = dctest.getBranches(); 
				for (String s: branches) {
					res.append(s);
				}
			}
		});

		
		
	
		JButton clearD = new JButton("Clear List");
		clearD.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (dDArray.size()>=1){
					dDArray.clear();
					dNArray.clear();
					dList.setText("");
				}
				else {
					dList.setText("There are no items, cannot clear.");
				}
			}

		});
		
		JButton removeLatestD = new JButton("Remove Last Entry");
		removeLatestD.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (dDArray.size()>1) {
					dDArray.remove(dDArray.size()-1);
					dNArray.remove(dNArray.size()-1);
					dList.setText(dList.getText().substring(0,dList.getText().lastIndexOf("\n")));
				}
				else if (dDArray.size()==1) {
					dList.setText("Cannot remove as there is only one item, please clear the list");
				}
				else {
					dList.setText("The list is empty, nothing to remove");
				}
				
			}

		});

		JPanel buttonsD = new JPanel(); 
		buttonsD.add(clearD);
		buttonsD.add(removeLatestD);
		buttonsD.add(calculateDistances); 
		distEntry.add(buttonsD);

		JPanel entries = new JPanel();
		entries.setLayout(new BoxLayout(entries, BoxLayout.X_AXIS));
		entries.add(taxaEntry);
		entries.add(distEntry);
		this.add(entries);
		this.add(sp);

		setVisible(true);
	}
	

	public static void main(String[] args) {
		JFrame jf = new JFrame();
		CalcPanel p = new CalcPanel();
		jf.add(p);
		jf.setTitle("Calculate Distances");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
		jf.setVisible(true);
	}

}
