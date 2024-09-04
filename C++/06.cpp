#include <iostream>
#include <vector>
#include <utility>
#include <algorithm>
using namespace std;

int rellenarTabla(int i, int j,
	vector<vector<int>>& tabla, vector<int> const& p, vector<int> const& o) {

	if (tabla[i][j] == -1) {
		if (p[i - 1] > j) tabla[i][j] = rellenarTabla(i - 1, j, tabla, p, o);
		else tabla[i][j] =
			max(rellenarTabla(i - 1, j, tabla, p, o), 
				rellenarTabla(i - 1, j - p[i - 1], tabla, p, o) + o[i - 1]);
	}
	return tabla[i][j];
}

pair<int, vector<pair<int,int>>> cazatesoros(vector<int> const& p, vector<int> const& o, int T) {
	// Nuestra solución es (x0,...,xN-1)
	// donde xi es la funcion caracteristica que indica si tomamos el tesoro i
	// maximizar Sum(xi * oi)
	// restriccion Sum(xi * pi) <= T
	// cazatesoros(k,t) ~ maximo oro usando tesoros 0..k con tiempo maximo t
	// cazatesoros(k,t) = 
	//	cazatesoros(k - 1, t) si pk > t
	//  max{cazatesoros(k-1,t), cazatesoros(k-1,t-pk) + ok}
	
	const int N = p.size();
	vector<vector<int>> tabla(N + 1, vector<int>(T + 1, -1));
	for (int i = 0; i < N + 1; ++i) tabla[i][0] = 0;
	for (int j = 0; j < T + 1; ++j) tabla[0][j] = 0;
	int maxOro = rellenarTabla(N, T, tabla, p, o);
	int i = N, j = T;
	vector<pair<int, int>> sol;
	while (i > 0) {
		if (p[i - 1] > j) --i;
		else if (tabla[i][j] == tabla[i - 1][j]) --i;
		else { sol.push_back({ p[i - 1] / 3, o[i - 1] }); j -= p[i - 1]; --i; }
	}
	std::reverse(sol.begin(), sol.end());
	return { maxOro, sol };
}

bool resuelveCaso() {
	int T, N;
	cin >> T;
	if (!cin) return false;
	cin >> N;
	vector<int> p(N), o(N);
	for (int i = 0; i < N; ++i) {
		int n;
		cin >> n >> o[i];
		p[i] = 3 * n;
	}
	auto [maxOro, sol] = cazatesoros(p, o, T);
	cout << maxOro << endl;
	cout << sol.size() << endl;
	for (auto const [pi, oi] : sol) cout << pi << ' ' << oi << endl;
	cout << "---\n";
	return true;
}

int main() {
	while (resuelveCaso());
	return 0;
}