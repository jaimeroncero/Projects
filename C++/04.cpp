#include <iostream>
#include <vector>
#include <memory>
#include <utility>
using namespace std;


pair<int, int> juegoDelTablero(vector<vector<int>> const& p) {
	const int N = p.size();
	unique_ptr<vector<int>> filaActual(new vector<int>(p[0])), filaAnterior(new vector<int>(N));
	

	for (int i = 1; i < N; ++i) {
		filaActual.swap(filaAnterior);
		*filaActual = p[i];
		filaActual->at(0) += max(filaAnterior->at(0), filaAnterior->at(1));
		for (int j = 1; j < N - 1; ++j)
			filaActual->at(j) += max(max(filaAnterior->at(j - 1), filaAnterior->at(j)), filaAnterior->at(j + 1));
		filaActual->at(N - 1) += max(filaAnterior->at(N - 2), filaAnterior->at(N - 1));
	}

	int suma = 0, casilla = 0;
	for(int i = 0; i < N; ++i)
		if (filaActual->at(i) > suma) {
			suma = filaActual->at(i);
			casilla = i;
		}

	return { suma, casilla };
}

bool resuelveCaso() {
	int N; cin >> N;
	if (!cin) return false;

	vector<vector<int>> p(N, vector<int>(N));
	for (auto& i : p) for (auto& j : i) cin >> j;
	auto [suma, casilla] = juegoDelTablero(p);
	cout << suma << ' ' << casilla + 1 << endl;
	return true;
}


int main() {
	while (resuelveCaso());
	return 0;
}