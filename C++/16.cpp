#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <map>
#include <climits>
#include <string>
#include <sstream>


using namespace std;

const int MOD = 1000000007;

bool resuelveCaso() {
    int N;

    cin >> N;

    if (!cin) return false;

    vector<vector<int>> board(4, vector<int>(N));
    for (int i = 0; i < 4; i++)
        for (int j = 0; j < N; j++)
            cin >> board[i][j];

    vector<vector<int>> dp(N, vector<int>(16, 0));

    for (int mask = 0; mask < 16; mask++) {
        if (((mask & (mask << 1)) == 0) && ((mask & (mask >> 1)) == 0))
            for (int i = 0; i < 4; i++)
                if ((mask >> i) & 1)
                    dp[0][mask] += board[i][0];
    }

    for (int j = 1; j < N; j++) {
        for (int mask = 0; mask < 16; mask++) {
            if (((mask & (mask << 1)) == 0) && ((mask & (mask >> 1)) == 0)) {
                for (int prev_mask = 0; prev_mask < 16; prev_mask++) {
                    if ((mask & prev_mask) == 0) {
                        int curr_sum = 0;
                        for (int i = 0; i < 4; i++)
                            if ((mask >> i) & 1)
                                curr_sum += board[i][j];
                        dp[j][mask] = max(dp[j][mask], dp[j - 1][prev_mask] + curr_sum);
                    }
                }
            }
        }
    }

    int max_sum = 0;
    for (int mask = 0; mask < 16; mask++)
        max_sum = max(max_sum, dp[N - 1][mask]);

    cout << max_sum << endl;

    return true;
}


int main() {
    // ajustes para que cin extraiga directamente de un fichero
#ifndef DOMJUDGE
    std::ifstream in("datos.txt");
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