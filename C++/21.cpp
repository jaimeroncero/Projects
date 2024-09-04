#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <map>
#include <climits>
#include <string>
#include <queue>


using namespace std;

struct tNodo {
    int k;
    int parejasActual;
    int cotaOptimista;
    vector<bool> libres;
    vector<int> ac;
};

bool operator <(const tNodo& a, const tNodo& b) {
    return a.cotaOptimista < b.cotaOptimista;
}

void calculaCota(const vector<vector<int>>& comensales, int m, vector<int>& acumulados) {
    vector<int> sumaMax(m, 0);

    for (int i = 0; i < m; i++) {
        for (int j = 0; j < i; j++) {
            if (comensales[i][j] == 1)
                sumaMax[i]++;
        }
    }

    acumulados = vector<int>(m, 0);
    for (int i = m - 2; i >= 0; i--) {
        acumulados[i] = acumulados[i + 1] + sumaMax[i + 1];
    }
}

void maxParejas(int n, int m, const vector<vector<double>>& distancias, const vector<vector<int>>& comensales, int& sol) {

    priority_queue<tNodo> colaAux;
    vector<int>acumulados;
    tNodo X, Y;
    Y.k = -1;
    Y.parejasActual = 0;
    Y.cotaOptimista = INT_MAX;
    Y.libres = vector<bool>(n, true);
    Y.ac = vector<int>(m, -1);

    colaAux.push(Y);
    calculaCota(comensales, m, acumulados);

    while (!colaAux.empty() && colaAux.top().cotaOptimista > sol) {
        Y = colaAux.top();
        colaAux.pop();

        X = Y;
        X.k++;

        for (int i = 0; i < n; i++) {
            if (X.libres[i]) {
                int nuevas = 0;
                bool aux = true;
                int j = 0;

                while (j < X.k && aux) {
                    if (comensales[X.k][j] == 0 && distancias[i][X.ac[j]] < 2.0) {
                        aux = false;
                    }
                    else if (comensales[X.k][j] == 1 && distancias[i][X.ac[j]] < 2.0) {
                        nuevas++;
                    }
                    j++;
                }

                if (aux) {
                    X.libres[i] = false;
                    X.ac[X.k] = i;
                    X.parejasActual = Y.parejasActual + nuevas;
                    X.cotaOptimista = X.parejasActual + acumulados[X.k];

                    if (X.cotaOptimista > sol) {
                        if (X.k == m - 1) {
                            sol = X.parejasActual;
                        }
                        else
                            colaAux.push(X);
                    }
                    X.libres[i] = true;
                    X.ac[X.k] = -1;
                }
            }
        }

    }

}

void resuelveCaso() {
    int n, m;

    cin >> n >> m;
    vector<vector<double>> distancias(n, vector<double>(n));
    for (int j = 0; j < n; ++j) {
        for (int k = 0; k < n; ++k) {
            cin >> distancias[j][k];
        }
    }

    vector<vector<int>> comensales(m, vector<int>(m));
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < m; j++) {
            cin >> comensales[i][j];
        }
    }
    int sol = -1;

    maxParejas(n, m, distancias, comensales, sol);
    if (sol == -1) {
        cout << "CANCELA" << '\n';
    }
    else {
        cout << "PAREJAS " << sol << '\n';
    }
    //return true;
}


int main() {
    int num;
    cin >> num;
    while (num--) resuelveCaso();
    return 0;
}