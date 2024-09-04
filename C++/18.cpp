#include <iostream>
#include <vector>
#include <queue>
#include <climits>
using namespace std;

struct carro { long long int anchura; };
struct camino { long long int anchura; };
struct nodo {
    vector<long long int> sol;
    long long int K;
    long long int calidad;
    long long int calidad_est;
    vector<bool> caminos_usados;

    bool operator < (const nodo& other) const { return this->calidad_est < other.calidad_est; }
};

long long int rp_quitanieves(const vector<vector<long long int>>& calidad, const vector<carro>& carros,
    const vector<camino>& caminos) {

    const long long int n = carros.size(), m = caminos.size();
    vector<long long int> suma_maximos(n + 1, 0);
    for (long long int i = n - 1; i >= 0; --i) {
        long long int max = -1;
        for (long long int j = 0; j < m; ++j) {
            if (carros[i].anchura <= caminos[j].anchura && calidad[i][j] > max) {
                max = calidad[i][j];
            }
        }
        suma_maximos[i] = suma_maximos[i + 1] + max;
    }

    nodo Y;
    Y.sol = vector<long long int>(n);
    Y.K = -1;
    Y.calidad = 0;
    Y.calidad_est = suma_maximos[0];
    Y.caminos_usados = vector<bool>(m, false);

    priority_queue<nodo> c;
    c.push(Y);
    long long int calidad_mejor = 0;

    while (!c.empty() && c.top().calidad_est > calidad_mejor) {
        Y = c.top();
        c.pop();

        if (Y.K == n - 1) {
            if (Y.calidad > calidad_mejor)
                calidad_mejor = Y.calidad;
        }

        else {
            for (long long int x_i = 0; x_i < m; ++x_i) {
                if (!Y.caminos_usados[x_i] && carros[Y.K + 1].anchura <= caminos[x_i].anchura) {
                    nodo X;
                    X.K = Y.K + 1;
                    X.caminos_usados = Y.caminos_usados;
                    X.caminos_usados[x_i] = true;
                    X.sol = Y.sol;
                    X.sol[X.K] = x_i;
                    X.calidad = Y.calidad + calidad[X.K][x_i];
                    X.calidad_est = X.calidad + suma_maximos[X.K + 1];

                    if (X.calidad_est > calidad_mejor) {
                        c.push(X);
                    }
                }
            }
        }
    }

    return calidad_mejor;
}

void resuelve_caso() {
    long long int n, m; cin >> n >> m;
    vector<carro> carros(n);
    vector<camino> caminos(m);
    for (auto& i : carros) cin >> i.anchura;
    for (auto& j : caminos) cin >> j.anchura;
    vector<vector<long long int>> calidades(n, vector<long long int>(m));
    for (auto& i : calidades)  for (auto& j : i) cin >> j;
    cout << rp_quitanieves(calidades, carros, caminos) << endl;
}

int main() {
    long long int num_casos; cin >> num_casos;
    for (long long int i = 0; i < num_casos; ++i)
        resuelve_caso();
    return 0;
}
