#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <map>
#include <climits>
#include <string>
#include <sstream>


using namespace std;

bool resuelveCaso() {
    int n, C;

    cin >> C >> n;

    if (!cin) return false;

    vector<int> monedas(n);
    for (int& m : monedas) cin >> m;

    vector<int> dp(C + 1, INT_MAX);
    vector<int> formas(C + 1, 0);
    dp[0] = 0;
    formas[0] = 1;

    for (int m : monedas) {
        for (int i = m; i <= C; ++i) {
            if (dp[i - m] != INT_MAX && dp[i - m] + 1 <= dp[i]) {
                if (dp[i - m] + 1 < dp[i]) formas[i] = 0;
                dp[i] = dp[i - m] + 1;
                formas[i] += formas[i - m];
            }
        }
    }

    if (dp[C] == INT_MAX) cout << 0 << endl;
    else cout << formas[C] << endl;


    return true;
}


int main() {
    while (resuelveCaso());
    return 0;
}