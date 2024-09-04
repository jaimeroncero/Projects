#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <sstream>

using namespace std;

// Función para encontrar la SCML entre dos vectores.
vector<string> longestCommonSubsequence(vector<string>& list1, vector<string>& list2) {

    vector<vector<int>> dp(list1.size() + 1, vector<int>(list2.size() + 1));

    for (int i = 1; i <= list1.size(); i++) {
        for (int j = 1; j <= list2.size(); j++) {
            if (list1[i - 1] == list2[j - 1]) {
                dp[i][j] = dp[i - 1][j - 1] + 1;
            }
            else {
                dp[i][j] = max(dp[i - 1][j], dp[i][j - 1]);
            }
        }
    }

    int i = list1.size();
    int j = list2.size();

    vector<string> sequence;

    while (i > 0 && j > 0) {
        if (list1[i - 1] == list2[j - 1]) {
            sequence.insert(sequence.begin(), list1[i - 1]);
            i--;
            j--;
        }
        else if (dp[i - 1][j] > dp[i][j - 1]) {
            i--;
        }
        else {
            j--;
        }
    }

    return sequence;
}


bool resuelveCaso() {
    string input1;
    getline(cin, input1);
    if (!cin) return false;
    string input2;
    getline(cin, input2);
    vector<string> list1, list2;
    stringstream ss1(input1);
    stringstream ss2(input2);
    string word;

    while (ss1 >> word) {
        list1.push_back(word);
    }

    while (ss2 >> word) {
        list2.push_back(word);
    }

    // Encuentra la longitud de la SCML
    vector<string> sequence = longestCommonSubsequence(list1, list2);

    // Imprime la SCML
    for (string song : sequence) {
        cout << song << " ";
    }

    return true;
}

int main() {
    while (resuelveCaso());
    return 0;
}
