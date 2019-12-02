
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.io.*;
import javax.swing.*;

public class CalcPanel extends JPanel {
	public CalcPanel() {
		this.setName("Distance Calculator");
		this.setSize(1500, 1500);
		// this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

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
		taxaEntry.add(descTaxa);

		JTextArea list = new JTextArea(5, 20);
		Color gray = Color.decode("#BBBEAE");
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
		taxaInput.add(list);

		taxaInput.setBackground(taxaColor);

		taxaEntry.add(taxaInput);

		taxa.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String i = input.getText();
				String j = name.getText();

				tArray.add(i);
				nArray.add(j);
				list.append("name: " + j + " taxa: " + i + "\n");

				input.setText("");
				name.setText("");
			}

		});

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
			}

		});

		taxaEntry.add(calculateTaxa);

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
		distInput.add(dList);

		distEntry.add(distInput);

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
				dList.append("name: " + j + " distances: " + i + "\n");

				dName.setText("");
				dDistances.setText("");
			}

		});

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
			}
		});

		distEntry.add(calculateDistances);

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
