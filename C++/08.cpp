#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

using matriz = vector<vector<int>>;

int comida(const vector<int>& c) {
	int n = c.size();
	matriz t = vector<vector<int>>(n, vector<int>(n, 0));
	for (int i = 0; i < n; ++i) t[i][i] = c[i];
	for (int i = 0; i < n - 1; ++i) t[i][i + 1] = std::max(c[i], c[i + 1]);
	for (int d = (n % 2 == 0) ? 3 : 2; d < n; d+=2) {
		for (int i = 0; i < n - d; ++i) {
			int j = i + d;
			int a, b;
			if (c[i + 1] > c[j]) a = t[i + 2][j] + c[i];
			else a = t[i + 1][j - 1] + c[i];
			if (c[i] > c[j - 1]) b = t[i + 1][j - 1] + c[j];
			else b = t[i][j - 2] + c[j];
			t[i][j] = std::max(a, b);
		}
	}

	return t[0][n - 1];
}

bool resuelveCaso() {
	int N; cin >> N;
	if (N == 0) return false;
	vector<int> c(N);
	for (auto& i : c) cin >> i;
	cout << comida(c) << endl;
	return true;
}

int main() {
	while (resuelveCaso());
	return 0;
}