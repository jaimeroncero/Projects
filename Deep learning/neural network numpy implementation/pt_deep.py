import torch
import torch.nn as nn
import torch.optim as optim
import numpy as np
import data
from matplotlib import pyplot as plt

class PTDeep(nn.Module):
    def __init__(self, layer_units, activation):
        """Params:
            layer_units (list of integers): number of units for each layer in the model
            activation (function): activation function for the hidden layers
        """
        super().__init__()  

        self.weights = nn.ParameterList()
        self.biases = nn.ParameterList()
        self.activation = activation

        for i in range(1, len(layer_units)):
            W = nn.Parameter(torch.rand((layer_units[i - 1], layer_units[i])))
            b = nn.Parameter(torch.zeros((1, layer_units[i])))
            self.weights.append(W)
            self.biases.append(b)

    def forward(self, X):
        # forward pass through the model: calculate probabilites
        logits = X
        for i, (W,b) in enumerate(zip(self.weights, self.biases)):
            logits = torch.mm(logits,W) + b
            if i < len(self.weights) - 1:
                logits = self.activation(logits)

        probs = torch.softmax(logits, dim=1)
        return probs 

    def get_loss(self, X, Yoh_):
        # NNL loss
        logits = X
        for i, (W,b) in enumerate(zip(self.weights, self.biases)):
            logits = torch.mm(logits,W) + b
            if i < len(self.weights) - 1:
                logits = self.activation(logits)

        max_logits, _ = torch.max(logits, dim=1, keepdim=True)  # (N, 1)
        stable_logits = logits - max_logits  # Stabilize logits
        log_probs = stable_logits - torch.log(torch.sum(torch.exp(stable_logits), dim=1, keepdim=True))  # Log-softmax
        loss = -torch.sum(Yoh_ * log_probs) 
        return loss

def train(model, X, Yoh_, param_niter, param_delta, param_lambda = 0):
    """Arguments:
        - X: model inputs [NxD], type: torch.Tensor
        - Yoh_: ground truth [NxC], type: torch.Tensor
        - param_niter: number of training iterations
        - param_delta: learning rate
    """
  
    # optimizer initialization
    optimizer = optim.SGD(model.parameters(), lr=param_delta, weight_decay=param_lambda)

    # training loop
    for i in range(param_niter):

        loss = model.get_loss(X, Yoh_)
        loss.backward()
        optimizer.step()
        optimizer.zero_grad()

        # Diagnostic trace
        if i % 100 == 0:
            print(f"Step: {i}, Loss: {loss.item():.4f}")

def eval(model, X):
  """Arguments:
     - model: type: PTLogreg
     - X: actual datapoints [NxD], type: np.array
     Returns: predicted class probabilites [NxC], type: np.array
  """
  X = torch.Tensor(X)
  with torch.no_grad():
      Y = model(X)
  return Y.detach().numpy()


if __name__ == "__main__":

    np.random.seed(100)

    # define input data X and labels Yoh_
    C = 2
    X, Y_ = data.sample_gmm_2d(6,C,10)
    Yoh_ = np.zeros((X.shape[0], C))
    Yoh_[np.arange(X.shape[0]), Y_] = 1

    # define the model:
    ptlr = PTDeep([2,10,10,2], torch.relu)

    # learn the parameters (X and Yoh_ have to be of type torch.Tensor):
    X = torch.Tensor(X)
    Yoh_ = torch.Tensor(Yoh_)
    train(ptlr, X, Yoh_, param_niter=10000, param_delta=0.0005, param_lambda=1e-3) 

    # get probabilites on training data
    probs = eval(ptlr, X)

    # visualize the results, decicion surface
    decfun = lambda X: np.argmax(eval(ptlr, X), axis=1)
    Y = decfun(X.detach().numpy())
    bbox = (np.min(X.detach().numpy(), axis=0), np.max(X.detach().numpy(), axis=0))
    data.graph_surface(decfun, bbox, offset=0.5)
    data.graph_data(X.detach().numpy(), Y_, Y)


    # print out the performance metric (precision and recall per class)
    accuracy, recall, precision = data.eval_perf_binary(Y,Y_)
    plt.title(f'Acc: {accuracy}\nRec: {recall}\nPrec: {precision}')

    plt.show()
