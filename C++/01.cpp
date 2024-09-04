#include<iostream>
#include<vector>
#include "EnterosInf.h"
using namespace std;

EntInf minPrecio(const int N, const int L, vector<int> const& costes, vector<int> const& longitudes) {

	vector<EntInf> tabla(L + 1, Infinito);

	for (int i = 0; i <= N; ++i){
		for (int j = L; j >= 0; --j) {

			if (j == 0) tabla[j] = 0;

			else if (i == 0) tabla[j] = Infinito;

			else if (longitudes[i - 1] <= j) 
				tabla[j] = std::min(tabla[j - longitudes[i - 1]] + costes[i - 1], tabla[j]);
		}
	}

	return tabla[L];

}

EntInf minNum(const int N, const int L, vector<int> const& costes, vector<int> const& longitudes) {

	vector<EntInf> tabla(L + 1, Infinito);

	for (int i = 0; i <= N; ++i) {
		for (int j = L; j >= 0; --j) {

			if (j == 0) tabla[j] = 0;

			else if (i == 0) tabla[j] = Infinito;

			else if (longitudes[i - 1] <= j) 
				tabla[j] = std::min(tabla[j - longitudes[i - 1]] + 1, tabla[j]);
		}
	}

	return tabla[L];
}

EntInf numSoluciones(const int N, const int L,
	vector<int> const& costes, vector<int> const& longitudes) {

	vector<EntInf> tabla(L + 1, Infinito);

	for (int i = 0; i <= N; ++i) {
		for (int j = L; j >= 0; --j) {

			if (j == 0) tabla[j] = 1;

			else if (i == 0) tabla[j] = 0;

			else if (longitudes[i - 1] <= j) 
				tabla[j] = tabla[j] + tabla[j - longitudes[i - 1]];
		
		}
	}

	return tabla[L];

}

bool resuelveCaso() {

	int N, L; cin >> N;
	if (!cin) return false;

	cin >> L;
	vector<int> costes(N), longitudes(N);
	for (int i = 0; i < N; ++i) {
		cin >> longitudes[i] >> costes[i];
	}

	EntInf numSol = numSoluciones(N, L, costes, longitudes); //O(NL)
	
	if (numSol == 0) {
		cout << "NO\n";
		return true;
	}

	EntInf precioMin = minPrecio(N, L, costes, longitudes); //O(NL)
	EntInf minNumero = minNum(N, L, costes, longitudes); //O(NL)

	cout << "SI " << numSol << ' ' << minNumero << ' ' << precioMin << '\n';

	return true;
}



int main() {

	while (resuelveCaso());
	return 0;
}