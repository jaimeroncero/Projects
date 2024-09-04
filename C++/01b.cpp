#include <iostream>
#include <vector>
#include "EnterosInf.h"
using namespace std;




EntInf matematico(int const L, vector<int> const& l) {
	//tabla de L+1 x N+1
	vector<vector<EntInf>> tabla(L + 1, vector<EntInf>(l.size() + 1));
	for (int i = 0; i < L + 1; ++i) tabla[i][0] = 0;
	for (int j = 0; j < l.size() + 1; ++j) tabla[0][j] = 1;
	for (int i = 1; i < L + 1; ++i)
		for (int j = 1; j < l.size() + 1; ++j) {
			if (l[j - 1] > i) tabla[i][j] = tabla[i][j - 1];
			else {
				tabla[i][j] = tabla[i][j - 1] + tabla[i - l[j - 1]][j - 1];
			}
		}
			
	return tabla[L][l.size()];
}

EntInf ingeniero(int L, vector<int> const& l) {
	vector<vector<EntInf>> tabla(L + 1, vector<EntInf>(l.size() + 1));
	for (int i = 1; i < L + 1; ++i) tabla[i][0] = EntInf::_intInf;
	for (int j = 0; j < l.size() + 1; ++j) tabla[0][j] = 0;
	for (int i = 1; i < L + 1; ++i)
		for (int j = 1; j < l.size() + 1; ++j) {
			if (l[j - 1] > i) tabla[i][j] = tabla[i][j - 1];
			else {
				tabla[i][j] = std::min(tabla[i][j - 1], tabla[i - l[j - 1]][j - 1] + 1);
			}
		}
	return tabla[L][l.size()];
}

EntInf economista(int L, vector<int> const& l, vector<int> const& c) {
	vector<vector<EntInf>> tabla(L + 1, vector<EntInf>(l.size() + 1));
	for (int i = 1; i < L + 1; ++i) tabla[i][0] = EntInf::_intInf;
	for (int j = 0; j < l.size() + 1; ++j) tabla[0][j] = 0;
	for (int i = 1; i < L + 1; ++i)
		for (int j = 1; j < l.size() + 1; ++j) {
			if (l[j - 1] > i) tabla[i][j] = tabla[i][j - 1];
			else {
				tabla[i][j] = std::min(tabla[i][j - 1], tabla[i - l[j - 1]][j - 1] + c[j - 1]);
			}
		}
	return tabla[L][l.size()];
}

bool resuelveCaso() {
	int N; cin >> N;
	if (!cin) return false;

	int L; cin >> L;
	vector<int> l(N), c(N);
	for (int i = 0; i < N; ++i) {
		cin >> l[i] >> c[i];
	}

	EntInf mat = matematico(L, l);
	if (mat == 0) {cout << "NO\n"; return true; }
	cout << "SI " << mat << ' ' << ingeniero(L,l) << ' ' << economista(L, l, c) << endl;

	return true;
}

int main() {
	while (resuelveCaso());
	return 0;
}