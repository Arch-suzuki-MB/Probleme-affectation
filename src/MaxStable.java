import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class MaxStable {

	short[][] C;
	static int n;
	static IloCplex modele;
	IloNumVar[] x;

	public MaxStable(short[][] c) {
		this.C = c;
		n = c.length;
		try {
			modele = new IloCplex();
			createModele();
			System.out.println(modele.toString());
		} catch (IloException e) {
			System.err.println(" Concert exception caught : " + e);
		}
	}

	public void createModele() {
		createVariables();
		createConstraints();
		createFonctionObj();
	}

	private void createFonctionObj() {
		try {
			IloLinearNumExpr fonction = modele.linearNumExpr();
			for (int i = 0; i < n; i++)
				fonction.addTerm(1, x[i]);
			modele.addMaximize(fonction);
		} catch (IloException e) {
			System.err.println(" Concert exception caught : " + e);
		}

	}

	private void createConstraints() {
		try {
			int cpt = 0;
			for (int l = 0; l < C.length; l++)
				for (int k = l + 1; k < C[0].length; k++) {
					IloLinearNumExpr expr = modele.linearNumExpr();
					if (C[l][k] != 0) {
						expr.addTerm(1.0, x[l]);
						expr.addTerm(1.0, x[k]);
						System.out.println(expr.toString());
						System.out.println(modele.addLe(expr, 1.0).toString());
						cpt++;
					}
				}
			System.out.println(" le nombre d'etiration : " + cpt);

		} catch (IloException e) {
			System.err.println(" Concert exception caught : " + e);
		}
	}

	private void createVariables() {
		try {
			x = modele.boolVarArray(n);
		} catch (IloException e) {
			System.err.println(" Concert exception caught : " + e);
		}

	}

	public boolean solve() throws IloException {
		boolean hasSolved = false;
		try {
			hasSolved = modele.solve();
		} catch (IloException e) {
			System.err.println(" Concert exception caught : " + e);
		}
		return hasSolved;

	}

	public double[] getSolution() throws IloException {

		double[] s = new double[n * n];
		try {
			s = modele.getValues(x);
		} catch (IloException ex) {
			System.err.println(" Concert exception caught : " + ex);
		}
		return s;

	}

	public void getsolutionStableMax() {
		try {
			if (solve()) {
				double[] d = getSolution();
				System.out.println(" La solution du probl`eme du stable maximum est :");
				System.out.print("S ={\t");
				for (int i = 0; i < n; i++) {
					if (d[i] == 1)
						System.out.print("v" + (i + 1) + "\t");
				}
				System.out.println("} ");
			}
		} catch (IloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		short[][] C = { { 0, 1, 1, 0, 0, 0, 0, 0 },
						{ 1, 0, 0, 0, 0, 1, 0, 0 }, 
						{ 1, 0, 0, 0, 0, 0, 1, 0 },
						{ 0, 0, 0, 0, 1, 0, 0, 1 }, 
						{ 0, 0, 0, 1, 0, 1, 0, 0 }, 
						{ 0, 1, 0, 0, 1, 0, 1, 0 },
						{ 0, 0, 1, 0, 0, 1, 0, 1 }, 
						{ 0, 0, 0, 1, 0, 0, 1, 0 } };

		MaxStable ex = new MaxStable(C);
		ex.getsolutionStableMax();
	}

}
