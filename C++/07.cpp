#include <iostream>
#include <vector>
#include <utility>
#include <algorithm>
#include <string>
#include <list>
using namespace std;

string reconstruir(const string& s, const vector<vector<int>>& t, const int i, const int j) {
	if (i == j) return string(1,s[i]);
	if (i == j + 1) return "";
	if (s[i] == s[j]) return s[i] + reconstruir(s, t, i + 1, j - 1) + s[i];
	if (t[i][j] == t[i + 1][j] + 1) return s[i] + reconstruir(s, t, i + 1, j) + s[i];
	return s[j] + reconstruir(s, t, i, j - 1) + s[j];
}

pair <int, string> palindromo(const string& s) {
	// Usaremos programacion dinamica razonando sobre subcadenas de longitud l
	// cada dim de la tabla corresponde a los indices i j
	// t[i][j] representa min inser para s[i..j]
	
	int n = s.size();
	vector<vector<int>> t(n, vector<int>(n));

	// solo usaremos la mitad triangular superior (sino repetimos casos y i <= j siempre)
	// caso base subcadenas de 1, necesitamos insertar 0

	for (int i = 0; i < n; ++i) t[i][i] = 0;

	// recorremos la tabla por diagonales (ascendente en longitud)
	// l = j - i + 1 -> j = i + l - 1

	for (int l = 2; l <= n; ++l) {
		for (int i = 0; i < n - l + 1; ++i) {
			int j = i + l - 1;

			// si tenemos una subcadena aSa, minInser(aSa) = minInser(S)
			
			if (s[i] == s[j]) t[i][j] = t[i + 1][j - 1];

			// minInser(aSb) = min{ minInser(aS), minInser(Sb) } + 1

			else t[i][j] = 1 + std::min(t[i + 1][j], t[i][j - 1]);
		}
	}

	

	return { t[0][n - 1], reconstruir(s, t, 0, n - 1) };

}



bool resuelveCaso() {
	string s; cin >> s;
	if (!cin) return false;
	auto [minIns, ps] = palindromo(s);
	cout << minIns << ' ' << ps << endl;
	return true;
}

int main() {
	while (resuelveCaso());
	return 0;
}