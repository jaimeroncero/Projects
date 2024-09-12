# Q-learning reinforcement learning algorithm

In this notebook Q-learning is used to achieve 99% success rate in the frozenLake gymnasium environment

![output](https://github.com/user-attachments/assets/d65e0299-2118-4d4b-bb8d-8acbd0da9f24)

The action space is discrete: [0,1,2,3] (up, right, down, left). The state space is also discrete [0,..,16]. 

Our goal is to fill the Q-table with accurate values. Q-table[state, action] represents $Q(s,a)$ function (total reward obtained from choosing action $a$ and go on choosing optimal actions each time).  $Q(s,a)=r+\gamma * \max_{a'}{Q(s',a')}$

$\epsilon$-greedy action selection was applied, that is, a random action is chosen with probability $\epsilon$ insted of taking the optimal one from the table.
This lets the algorithm explore new posibilities even if a good strategy was found. This $\epsilon$ probability decreases with the number of steps.
