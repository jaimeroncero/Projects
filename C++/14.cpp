#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <map>
#include <limits>
#include <string>
#include <sstream>


using namespace std;

const int MOD = 1000000007;

int resolver(const int& N, const int& F) {

    vector<int> vectorAux(N, 0);
    vectorAux[0] = 1;

    for (int i = 1; i < N; i++) {
        for (int k = max(0, i - F - 1); k < i; k++) {
            vectorAux[i] = (vectorAux[i] + vectorAux[k]) % 1000000007;
        }
    }

    return vectorAux[N - 1];
}

bool resuelveCaso() {
    int n, m;
    cin >> n >> m;
    if (n == 0 && m == 0) return false;

    cout << resolver(n, m) << endl;


    return true;
}


int main() {
    while (resuelveCaso());
    return 0;
}