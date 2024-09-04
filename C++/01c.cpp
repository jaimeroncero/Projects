#include <iostream>
#include <vector>
#include <algorithm> // Para std::min

using namespace std;

long long int const infinito = 1e18 + 2;
struct Tcuerda {
    int l;
    int precio;
};

struct TInfoMatriz {
    long long int c;
    long long int minN;
    long long int minP;
};

bool resuelveCaso() {
    int N, L;
    cin >> N;
    if (!cin) return false;
    cin >> L;
    vector<Tcuerda> cuerdas(N + 1);
    for (int i = 1; i <= N; ++i) {
        cin >> cuerdas[i].l >> cuerdas[i].precio;
    }

    vector<TInfoMatriz> prev(1 + L, { 0, infinito, infinito }), curr(1 + L, { 0, infinito, infinito });
    prev[0] = { 1, 0, 0 }; // Inicialización para la primera cuerda

    for (int i = 1; i <= N; ++i) {
        for (int j = 1; j <= L; ++j) {
            if (cuerdas[i].l > j) {
                curr[j] = prev[j];
            }
            else {
                curr[j].c = prev[j].c + prev[j - cuerdas[i].l].c;
                curr[j].minN = min(prev[j].minN, prev[j - cuerdas[i].l].minN + 1);
                curr[j].minP = min(prev[j].minP, prev[j - cuerdas[i].l].minP + cuerdas[i].precio);
            }
        }
        prev = curr; // Actualiza el vector previo para la siguiente iteración
    }

    TInfoMatriz sol = prev[L];
    if (sol.c == 0) {
        cout << "NO";
    }
    else {
        cout << "SI " << sol.c << " " << sol.minN << " " << sol.minP;
    }
    cout << endl;
    return true;
}

int main() {
    while (resuelveCaso());
    return 0;
}
