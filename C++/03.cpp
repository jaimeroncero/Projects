#include <iostream>
#include <vector>
using namespace std;

int esquivandoLasObras(vector<vector<char>> const& ciudad) {
	int N = ciudad.size(), M = ciudad[0].size();

	//nºcaminos(0,0,L) = nºcaminos(1,0,L-1) + nºcaminos(0,1,L-1)
	vector<vector<int>> caminos(N, vector<int>(M));
	caminos[N - 1][M - 1] = 1;
	for(int j = M - 2 ; j >= 0; --j)
		caminos[N - 1][j] = (ciudad[N - 1][j] == 'X') ? 0 : caminos[N - 1][j + 1];
	for (int i = N - 2; i >= 0; --i) {
		if (ciudad[i][M - 1] == 'X') caminos[i][M - 1] = 0;
		else caminos[i][M - 1] = caminos[i + 1][M - 1];
		for (int j = M - 2; j >= 0; --j) {
			if (ciudad[i][j] == 'X') caminos[i][j] = 0;
			else caminos[i][j] = caminos[i][j + 1] + caminos[i + 1][j];
		}
	}

	return caminos[0][0];
}

bool resuelveCaso() {
	int N, M;
	cin >> N;
	if (!cin) return false;
	cin >> M;
	vector<vector<char>> ciudad(N, vector<char>(M));
	for (auto& i : ciudad) for (auto& j : i) cin >> j;
	cout << esquivandoLasObras(ciudad) << endl;
	return true;
}

int main() {
	while (resuelveCaso());
	return 0;
}