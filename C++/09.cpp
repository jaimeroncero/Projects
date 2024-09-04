#include <iostream>
#include <vector>
#include <unordered_map>
#include <string>
#include <algorithm>
#include <memory>
#include "EnterosInf.h"

using namespace std;


bool resuelveCaso() {
	int P, R; 
	cin >> P;
	if (!cin) return false;
	cin >> R;
	unique_ptr<vector<vector<EntInf>>> CAnt(new vector<vector<EntInf>>(P, vector<EntInf>(P, Infinito)));
	unique_ptr<vector<vector<EntInf>>> CAct(new vector<vector<EntInf>>(P, vector<EntInf>(P, Infinito)));
	unordered_map<string, int> m;

	int cont = 0;
	while (R > 0) {
		string n1, n2; cin >> n1 >> n2;
		if (!m.count(n1)) {
			m[n1] = cont;
			++cont;
		}
		if (!m.count(n2)) {
			m[n2] = cont;
			++cont;
		}
		int v1 = m[n1], v2 = m[n2];
		CAnt->at(std::min(v1, v2)).at(std::max(v1, v2)) = 1;
		--R;
	}

	
	for (int i = 0; i < P; ++i) {
		CAnt->at(i).at(i) = 0;
		CAct->at(i).at(i) = 0;
	}
	



	for (int k = 1; k <= P; ++k) {
		for (int i = 0; i < P; ++i) {
			for (int j = i + 1; j < P; ++j) {
				CAct->at(i).at(j) = std::min(CAnt->at(i).at(j), CAnt->at(i).at(k - 1) + CAnt->at(k - 1).at(j));
			}
		}

		CAnt.swap(CAct);
	}

	CAnt.swap(CAct);

	/*
	for (auto i : *CAnt) {
		for (auto j : i) cout << j << ' ';
		cout << endl;
	}
	*/

	EntInf max = CAct->at(0).at(P - 1);
	for (int i = 0; i < P; ++i) {
		for (int j = i + 1; j < P; ++j) {
			if (CAct->at(i).at(j) > max) max = CAct->at(i).at(j);
		}
	}

	if (max == Infinito) cout << "DESCONECTADA\n";
	else cout << max << endl;

	
	return true;
}

int main() {

	while (resuelveCaso());
	return 0;
}
