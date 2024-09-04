#include<iostream>
#include<vector>
#include "EnterosInf.h"
using namespace std;

void dardos(const int N, const int S, vector<int> const& puntuaciones) {

	vector<vector<EntInf>> tabla(S + 1, vector<EntInf>(N + 1, Infinito));

	for (int i = 0; i <= S; ++i)
		for (int j = 0; j <= N; ++j) {

			if (j == 0) tabla[i][j] = 0;

			else if (i == 0) tabla[i][j] = Infinito;

			else if (puntuaciones[i - 1] > j) tabla[i][j] = tabla[i - 1][j];

			else tabla[i][j] =
				std::min(tabla[i - 1][j], tabla[i][j - puntuaciones[i - 1]] + 1);

		}


	if (tabla[S][N] == Infinito) cout << "Imposible\n";

	else {
		vector<EntInf> sol;
		int i = S, j = N;
		while (i > 0) {
			if (tabla[i][j] == tabla[i - 1][j]) --i; //no hemos usado i

			else {
				sol.push_back(puntuaciones[i - 1]);
				j -= puntuaciones[i - 1];
			}
		}
		cout << tabla[S][N] << ": ";
		for (auto const e : sol) cout << e << ' ';
		cout << '\n';
	}

}

bool resuelveCaso() {

	int N, S; cin >> N;

	if (!cin) return false;

	cin >> S;
	vector<int> puntuaciones(S);
	for (auto& i : puntuaciones) cin >> i;

	/*
	* Dardos(i,j) ~ minimo numero de dardos para conseguir una cantidad j
	*	sumando sectores del {0,...,i}
	* 
	* Dardos(i,j) = min{Dardos(i-1,j),
	*	Dardos(i,j - puntuacions[i-1]) + 1} si P > j
	* 
	* Dardos(i,0) = 0
	* Dardos(0,j) = inf
	* 
	*/

	dardos(N, S, puntuaciones); //O((N+1)(S+1))

	return true;
}



int main() {

	while (resuelveCaso());
	return 0;
}