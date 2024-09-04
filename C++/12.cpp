
/*@ <answer>
 *
 * Nombre y Apellidos: Rodrigo Sangil Terr�s
 *
 *@ </answer> */

#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <string>
#include <algorithm>
using namespace std;

 // propios o los de las estructuras de datos de clase

/*@ <answer>

 Escribe aqu� un comentario general sobre la soluci�n, explicando c�mo
 se resuelve el problema y cu�l es el coste de la soluci�n, en funci�n
 del tama�o del problema.

 @ </answer> */

struct Pelicula {
	int s, d, e;

	bool operator<(Pelicula const p) const {
		if (s < p.s) return true;
		else return false;
	}
};


 // ================================================================
 // Escribe el c�digo completo de tu soluci�n aqu� debajo
 // ================================================================
 //@ <answer>
template <typename T>
int findLowerBoundPosition(std::vector<T> const& vec, int startPos, int endPos, T const& value) {
	// Ensure the start and end positions are within the bounds of the vector
	if (startPos < 0 || startPos >= vec.size() || endPos > vec.size() || startPos > endPos) {
		throw std::out_of_range("Invalid range for the vector");
	}

	// Perform a binary search within the range [startPos, endPos)
	int left = startPos;
	int right = endPos;
	while (left < right) {
		int mid = left + (right - left) / 2;
		if (vec[mid] < value) {
			left = mid + 1;
		}
		else {
			right = mid;
		}
	}

	// 'left' is the position of the lower bound
	return left;
}

int cine(vector<Pelicula> const& P, int N) {
	vector<int> v(N + 1, 0);
	v[N - 1] = P[N - 1].d;
	for (int i = N - 2; i >= 0; i--) {
		Pelicula p = P[i];
		int j = findLowerBoundPosition(P, i + 1, N, Pelicula({ p.e, 0, 0}));
		v[i] = max(v[i + 1], v[j] + p.d);
	}
	return v[0];
}

int TimeToMinutes(const std::string& time) {
	int hours = std::stoi(time.substr(0, 2));
	int minutes = std::stoi(time.substr(3, 2));
	return hours * 60 + minutes;
}

bool resuelveCaso() {

	// leer los datos de la entrada
	int N;
	cin >> N;

	if (N == 0)  // fin de la entrada
		return false;

	vector<Pelicula> P(N);
	for (auto& p : P) {
		string aux;
		cin >> aux;
		p.s = TimeToMinutes(aux);
		cin >> p.d;
		p.e = p.s + p.d + 10;
	}

	// resolver el caso posiblemente llamando a otras funciones
	sort(P.begin(), P.end());
	int sol = cine(P, N);

	// escribir la soluci�n
	cout << sol << '\n';

	return true;
}

//@ </answer>
//  Lo que se escriba dejado de esta l�nea ya no forma parte de la soluci�n.

int main() {
	// ajustes para que cin extraiga directamente de un fichero
#ifndef DOMJUDGE
	std::ifstream in("casos.txt");
	auto cinbuf = std::cin.rdbuf(in.rdbuf());
#endif

	while (resuelveCaso());

	// para dejar todo como estaba al principio
#ifndef DOMJUDGE
	std::cin.rdbuf(cinbuf);
	system("PAUSE");
#endif
	return 0;
}
