import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class ProbAffectation {
	double[][] C;
	int n;
	int m;
	double val_Optimal = 0;
	IloCplex modele;
	IloNumVar[][] x;

	public ProbAffectation(double[][] c) {
		C = c;
		this.n = c.length;
		this.m = c[0].length;
		try {
			x = new IloNumVar[n][m];
			this.modele = new IloCplex();
		} catch (IloException e) {
			e.printStackTrace();
		}
		createModek();

	}

	private void createModek() {

		CreateVariables();
		CreateConstraints();
		CreateFonctionObjective();
	}

	private void CreateFonctionObjective() {
		try {
			IloLinearNumExpr S = modele.linearNumExpr();
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) {
					S.addTerm(C[i][j], x[i][j]);
				}
			}
			modele.addMinimize(S);
		} catch (IloException e) {
			e.printStackTrace();
		}

	}

	private void CreateConstraints() {
		try {
			for (int i = 0; i < m; i++) {
				IloLinearNumExpr f1 = modele.linearNumExpr();
				for (int j = 0; j < n; j++) {
					f1.addTerm(1, x[i][j]);
				}
				modele.addEq(f1, 1);
			}
			for (int j = 0; j < n; j++) {
				IloLinearNumExpr f2 = modele.linearNumExpr();
				for (int i = 0; i < m; i++) {
					f2.addTerm(1, x[i][j]);
				}
				modele.addEq(f2, 1);
			}

		} catch (IloException e) {
			e.printStackTrace();
		}
	}

	private void CreateVariables() {
		try {
			for (int i = 0; i < n; i++) {

				x[i] = modele.boolVarArray(m);

			}
		} catch (IloException e) {
			e.printStackTrace();
		}
	}

	public boolean Solve() throws IloException {
		return modele.solve();
	}

	public double[][] getSolution() throws IloException {
		double d[][] = new double[n][m];
		if (Solve()) {
			val_Optimal = modele.getObjValue();
			for (int i = 0; i < n; i++) {
				d[i] = modele.getValues(x[i]);
			}
		}
		return d;
	}

	public void getSolutionFin() throws IloException {
		double d[][] = getSolution();
		for (int i = 0; i < d.length; i++) {
			for (int j = 0; j < d.length; j++) {
				System.out.print("\t" + d[i][j]);
			}
			System.out.println();
		}
	}

	public double getVal_Optimale() {
		return val_Optimal;
	}

	public static void main(String[] args) throws IloException {
		double[][] M = { { 15, 40, 5, 20, 20 }, { 22, 33, 9, 16, 20 }, { 40, 6, 28, 0, 26 }, { 8, 0, 7, 25, 60 },
				{ 10, 10, 60, 15, 5 } };

		ProbAffectation e = new ProbAffectation(M);
		System.out.println("*******************************************************************");
		e.getSolutionFin();
		System.out.println("Valeur optimale est :" + e.getVal_Optimale());
	}

}
