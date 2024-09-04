#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
using namespace std;

struct tNodo {
	int t;
	int tOpt;
	int k;
	vector<bool> marcas;
};

bool operator < (tNodo const& X, tNodo const& Y) { return X.tOpt < Y.tOpt; }


int funcionariosRamYPoda(vector<vector<int>> const& fun2t) {
	priority_queue<tNodo> cp;
	const int N = fun2t.size();

	//precalculo estimaciones
	vector<int> opt(N + 1, 0);
	for (int i = N - 1; i >= 0; --i) opt[i] = *std::min_element(fun2t[i].begin(), fun2t[i].end()) + opt[i + 1];
	vector<int> pes(N + 1, 0);
	for (int i = N - 1; i >= 0; --i) pes[i] = *std::max_element(fun2t[i].begin(), fun2t[i].end()) + pes[i + 1];

	// Generamos la raiz
	tNodo Y;
	Y.k = -1; 
	Y.marcas = vector<bool>(N, false);
	Y.t = 0;
	Y.tOpt = opt[0];
	cp.push(Y);
	int tMejor = pes[0];

	while (!cp.empty() && cp.top().tOpt <= tMejor) {
		Y = cp.top(); cp.pop();

		// Generamos los hijos de Y
		tNodo X;
		X.k = Y.k + 1; 
		X.marcas = Y.marcas;
		for (int i = 0; i < N; ++i)
			if (!X.marcas[i]) {
				X.marcas[i] = true;
				X.t = Y.t + fun2t[X.k][i];
				X.tOpt = X.t + opt[X.k + 1];

				if (X.tOpt <= tMejor) {
					if (X.k == N - 1) {
						tMejor = X.t;
					}
					else {
						cp.push(X);
						tMejor = std::min(tMejor, X.t + pes[X.k + 1]);
					}
				}

				X.marcas[i] = false;
			}
	}

	return tMejor;
	
}

bool resuelveCaso() {
	int N; cin >> N;
	if (N == 0) return false;
	vector<vector<int>> fun2t(N, vector<int>(N));
	for (auto& i : fun2t) for (auto& j : i) cin >> j;
	cout << funcionariosRamYPoda(fun2t) << endl;
	return true;
}

int main() {
	while (resuelveCaso());
	return 0;
}