#include <iostream>
#include <vector>
using namespace std;

int abajoDerecha(vector<vector<int>> const& tabla) {
	int N = tabla.size(), M = tabla[0].size();

	vector<vector<int>> caminos(N, vector<int>(M, 0));
	caminos[N - 1][M - 1] = 1;
	for (int j = M - 2; j >= 0; --j)
		if (tabla[N - 1][j] < M - j) caminos[N - 1][j] =  caminos[N - 1][j + tabla[N - 1][j]];
	for (int i = N - 2; i >= 0; --i) {
		if (tabla[i][M - 1] < N - i) caminos[i][M - 1] = caminos[i + tabla[i][M - 1]][M - 1];
		for (int j = M - 2; j >= 0; --j) {
			int s = tabla[i][j];
			if (s < M - j) caminos[i][j] += caminos[i][j + s];
			if (s < N - i) caminos[i][j] += caminos[i + s][j];
		}
	}

	return caminos[0][0];
}

bool resuelveCaso() {
	int N, M;
	cin >> N;
	if (!cin) return false;
	cin >> M;
	vector<vector<int>> tabla(N, vector<int>(M));
	for (auto& i : tabla) for (auto& j : i) cin >> j;
	cout << abajoDerecha(tabla) << endl;
	return true;
}

int main() {
	while (resuelveCaso());
	return 0;
}