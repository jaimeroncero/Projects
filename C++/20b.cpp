#include<iostream>
#include<vector>
#include<queue>
#include<climits>
using namespace std;

struct nodo {
    vector<int> sol;
    int k;
    vector<short int> cont_supermercados; // inicia a 0, si vale 0..2 se puede comprar otro
    int precio;
    int precio_estimado;

    bool operator < (const nodo& other) const { return this->precio_estimado > other.precio_estimado; }
};

int calcular_coste_estimado(vector<vector<int>> const& precios, nodo const& X) {
    const int n = precios[0].size();
    const int m = precios.size();
    int estimacion = X.precio;
    for (int i = X.k + 1; i < n; ++i) {
        int precio_min = INT_MAX;
        for (int j = 0; j < m; ++j) {
            if (X.cont_supermercados[j] < 3 && precios[j][i] < precio_min) {
                precio_min = precios[j][i];
            }
        }
        estimacion += precio_min;
    }
    return estimacion;
}

int calcular_coste_pesimista(vector<vector<int>> const& precios, nodo const& X) {
    const int n = precios[0].size();
    const int m = precios.size();
    int estimacion_pesimista = X.precio;
    for (int i = X.k + 1; i < n; ++i) {
        int precio_max = 0;
        for (int j = 0; j < m; ++j) {
            if (X.cont_supermercados[j] < 3 && precios[j][i] > precio_max) {
                precio_max = precios[j][i];
            }
        }
        estimacion_pesimista += precio_max;
    }
    return estimacion_pesimista;
}

int rp_compra_de_la_semana(const vector<vector<int>>& precios) {
    const int n = precios[0].size();
    const int m = precios.size();
    nodo Y;
    Y.sol = vector<int>(n);
    Y.k = -1;
    Y.cont_supermercados = vector<short int>(m, 0);
    Y.precio = 0;
    Y.precio_estimado = calcular_coste_estimado(precios, Y);

    priority_queue<nodo> cp;
    cp.push(Y);
    int precio_mejor = INT_MAX;

    while (!cp.empty()) {
        Y = cp.top();
        cp.pop();

        int cota_pesimista = calcular_coste_pesimista(precios, Y);
        if (cota_pesimista >= precio_mejor) continue; // Podamos la rama si la cota pesimista es mayor o igual al mejor precio

        if (Y.k == n - 1) {
            if (Y.precio < precio_mejor) {
                precio_mejor = Y.precio;
            }
        }
        else {
            // Compramos el producto K + 1 en el supermercado i
            for (int i = 0; i < m; ++i) {
                if (Y.cont_supermercados[i] < 3) {
                    nodo X;
                    X.k = Y.k + 1;
                    X.sol = Y.sol;
                    X.sol[X.k] = i;
                    X.precio = Y.precio + precios[i][X.k];
                    X.cont_supermercados = Y.cont_supermercados;
                    ++X.cont_supermercados[i];
                    X.precio_estimado = calcular_coste_estimado(precios, X);

                    // Si es prometedor
                    if (X.precio_estimado < precio_mejor) {
                        cp.push(X);
                    }
                }
            }
        }
    }

    return precio_mejor;
}

void resuelve_caso() {
    int m, n; cin >> m >> n;
    vector<vector<int>> precios(m, vector<int>(n));
    for (auto& i : precios) for (auto& j : i) cin >> j;
    cout << rp_compra_de_la_semana(precios) << endl;
}

int main() {
    int num_casos; cin >> num_casos;
    for (int i = 0; i < num_casos; ++i) resuelve_caso();
    return 0;
}
