import data
import numpy as np
from matplotlib import pyplot as plt

def fcann2_train(X, Y_):
    """
    Trains a 2-layer classification model using stochastic gradient descent.

    Args:
        X: Data, np.array (N x D)
        Y_: Class indices, np.array (N,)

    Returns:
        W_0, b_0: Parameters of the first layer (weights and biases)
        W_1, b_1: Parameters of the second layer (weights and biases)
    """
    # Hyperparameters
    param_niter = int(2e5)  # Number of iterations
    param_delta = 0.005     # Learning rate
    hidden_units = 30        # Number of hidden layer units
    param_lambda = 1e-3    # Regularization coefficient

    # Dataset dimensions
    N, D = X.shape
    C = np.max(Y_) + 1  

    # One-hot encoding for labels
    Yoh_ = np.zeros((N, C))
    Yoh_[np.arange(N), Y_] = 1

    # Initialize weights and biases
    W_0 = np.random.randn(hidden_units, D) 
    b_0 = np.zeros((hidden_units, 1))
    W_1 = np.random.randn(C, hidden_units) 
    b_1 = np.zeros((C, 1))

    for i in range(param_niter):
        # Select a random training example
        example_idx = np.random.randint(low=0, high=N)
        x = X[example_idx, :].reshape(D, 1)  
        yoh = Yoh_[example_idx, :].reshape(C, 1) 

        # Forward pass
        s_0 = W_0 @ x + b_0  # Hidden layer pre-activation (hidden_units x 1)
        h_0 = np.maximum(0, s_0)  # ReLU activation (hidden_units x 1)
        s_1 = W_1 @ h_0 + b_1  # Output layer pre-activation (C x 1)
        exp_s1 = np.exp(s_1 - np.max(s_1))  # Stable softmax
        h_1 = exp_s1 / np.sum(exp_s1)  # Output probabilities (C x 1)

        # Diagnostic trace
        if i % 1000 == 0:
            logits = fcann2_classify(X, W_0, b_0, W_1, b_1)
            loss = -np.sum(Yoh_ * np.log(logits + 1e-10))  
            print(f"Iteration {i}: Loss = {loss:.4f}")

        # Backward pass
        dL_ds1 = h_1 - yoh  
        dL_dW1 = dL_ds1 @ h_0.T + param_lambda * W_1  
        dL_db1 = dL_ds1  

        dL_dh0 = W_1.T @ dL_ds1  
        dL_ds0 = dL_dh0 * (s_0 > 0)  
        dL_dW0 = dL_ds0 @ x.T + param_lambda * W_0 
        dL_db0 = dL_ds0  

        # Parameters update
        W_0 -= param_delta * dL_dW0
        b_0 -= param_delta * dL_db0
        W_1 -= param_delta * dL_dW1
        b_1 -= param_delta * dL_db1

    return W_0, b_0, W_1, b_1


def ReLU(S):
    return np.maximum(S, 0)


def softmax(S):
    expscores = np.exp(S - np.max(S, axis=1, keepdims=True))
    return expscores / np.sum(expscores, axis=1, keepdims=True)


def fcann2_classify(X, W_0, b_0, W_1, b_1):
    S_0 = X @ W_0.T + b_0.T
    H_0 = ReLU(S_0)
    S_1 = H_0 @ W_1.T + b_1.T
    return softmax(S_1)


def fcann2_decfun(W_0, b_0, W_1, b_1):
    def classify(X):
        return np.argmax(fcann2_classify(X, W_0, b_0, W_1, b_1), axis=1)
    return classify


if __name__ == "__main__":
    np.random.seed(100)

    # Load dataset
    X, Y_ = data.sample_gmm_2d(ncomponents=6, nclasses=2, nsamples=10)

    # Train model
    W_0, b_0, W_1, b_1 = fcann2_train(X, Y_)

    # Classify points
    probs = fcann2_classify(X, W_0, b_0, W_1, b_1)
    decfun = fcann2_decfun(W_0, b_0, W_1, b_1)
    Y = np.argmax(probs, axis=1)

    # Plot decision surface
    rect = (np.min(X, axis=0), np.max(X, axis=0))
    data.graph_surface(fcann2_decfun(W_0, b_0, W_1, b_1), rect, offset=0.5)

    # Plot data points
    data.graph_data(X, Y_, Y)
    plt.show()
