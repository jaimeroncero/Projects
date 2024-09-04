#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <queue>


using namespace std;

struct tNodo {
    int k;
    int basuraRecogida;
    int cotaOptimista;
    int limpias;
    vector<int> sol;
    vector<int> playas;
};

bool operator<(const tNodo& a, const tNodo& b) {
    return a.cotaOptimista < b.cotaOptimista;
}

vector<int> precalculoestimaciones(const vector<vector<int>>& personas) {
    int n = personas.size();
    vector<int> sumaMax(n, 0);
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < personas[i].size(); ++j) {
            sumaMax[i] = max(sumaMax[i], personas[i][j]);
        }
    }
    vector<int> est(n + 1, 0);
    for (int i = n - 1; i >= 0; i--) {
        est[i] = est[i + 1] + sumaMax[i];
    }
    return  est;
}


void maxBasura(int n, int m, const vector<vector<int>>& personas, int L, vector<int>& basura) {
    priority_queue<tNodo> pq;
    tNodo nodo, hijo;
    auto sumaMax = precalculoestimaciones(personas);
    vector<int> sol_mejor(n, -1);
    int playasLimpias_mejor = 0;
    nodo.playas = basura;
    int basura_mejor = 0;
    nodo.k = -1;
    nodo.basuraRecogida = 0;
    nodo.sol = vector<int>(n, -1);
    nodo.cotaOptimista = nodo.basuraRecogida + sumaMax[nodo.k + 1];
    nodo.limpias = 0;
    for (auto x : nodo.playas) if (x == 0) nodo.limpias++;
    pq.push(nodo);

    while (!pq.empty() && pq.top().cotaOptimista >= basura_mejor) {
        nodo = pq.top();
        pq.pop();
        hijo = nodo; hijo.k++;
        for (int i = 0; i < m; i++) {
            if (hijo.playas[i] > 0) {
                hijo.sol[hijo.k] = i;
                hijo.basuraRecogida += min(personas[hijo.k][i], hijo.playas[i]);
                hijo.playas[i] -= min(personas[hijo.k][i], hijo.playas[i]);
                hijo.cotaOptimista = hijo.basuraRecogida + sumaMax[hijo.k + 1];
                if (hijo.playas[i] == 0) hijo.limpias++;
                if (hijo.cotaOptimista > basura_mejor) {
                    if (hijo.k == n - 1) {
                        if ((hijo.basuraRecogida >= basura_mejor) && (L <= hijo.limpias)) {
                            sol_mejor = hijo.sol;
                            basura_mejor = hijo.basuraRecogida;
                            playasLimpias_mejor = hijo.limpias;
                        }
                    }
                    else {
                        pq.push(hijo);
                        basura_mejor = max(basura_mejor, hijo.basuraRecogida);
                    }
                }
                hijo = nodo; hijo.k++;

            }
        }
    }


    if (playasLimpias_mejor >= L) {
        cout << basura_mejor << ' ' << playasLimpias_mejor << endl;
    }
    else {
        cout << "IMPOSIBLE" << endl;
    }
}

void resuelveCaso() {
    int n, m, l;
    cin >> n >> m >> l;

    vector<int> basura(m);
    for (int j = 0; j < m; ++j) {
        cin >> basura[j];
    }

    vector<vector<int>> personas(n, vector<int>(m));
    for (int j = 0; j < n; ++j) {
        for (int k = 0; k < m; ++k) {
            cin >> personas[j][k];
        }
    }

    maxBasura(n, m, personas, l, basura);
}


int main() {
    // ajustes para que cin extraiga directamente de un fichero
#ifndef DOMJUDGE
    std::ifstream in("datos.txt");
    auto cinbuf = std::cin.rdbuf(in.rdbuf());
#endif

    int num;
    cin >> num;

    while (num--) resuelveCaso();

    //while (resuelveCaso());
    // 
    // para dejar todo como estaba al principio
#ifndef DOMJUDGE
    std::cin.rdbuf(cinbuf);
    system("PAUSE");
#endif
    return 0;
}