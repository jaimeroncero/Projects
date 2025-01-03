# Neural network numpy implementation
Design of a 2-layer fully connected artificial neural network using numpy. 

## Contents
- [Model architecture](model-architecture)
- [Loss function](loss_function)
- [Optimization method](optimization_method)
- [Modules](modules)


### Model architecture
The objective is to design a simple ANN by hand for classification tasks. Our model architecture will be a 2-layer fully connected artificial neural network. We are using ReLU activation for 1st layer and softmax for 2nd layer. The number of units for the hidden layer is an hyperparameter of the model:
- $$s_0=W_0* x + b_0$$
- $$h_0=ReLU(s_0)$$
- $$s_1=W_1*h_0+b_1$$
- $$h_1=softmax(s_1)$$

| Parameter | Dimension |
|:----------|:----------|
| $x$ (input) | $D \times 1$ |
| $W_0$ | $H \times D$ |
| $b_0$ | $H \times 1$ |
| $s_0$ | $H \times 1$ |
| $h_0$ | $H \times 1$ |
| $W_1$ | $C \times H$ |
| $b_1$ | $C \times 1$ |
| $s_1$ | $C \times 1$ |
| $h_1$ (output)| $C \times 1$ |

**D**: length of each training example.  
**H**: Number of units in hidden layer (hyperparameter).  
**C**: Number of output classes.   

<img src="https://github.com/user-attachments/assets/a100af33-a701-4b1c-af54-f7af606d1845" alt="Image" width="300">

### Loss function
We can interpret the softmax's output as the vector of probabilities that the example $x$ belongs to each class:
$\mathbf{h_1}_i = P(Y=c_i|\mathbf{x})$. We will optimize **negative log-likelihood** loss:    

$$ L = \sum_{i=0}^{N-1}{-Y_i \cdot \log(P(Y=Y_i|X_i))}  = \sum_{i=0}^{N-1}{\sum_{j=0}^{C-1} -Yoh_{i,j} \cdot \log(h_1(X_i))} $$

Here, we are interpretating our model as a random variable $Y$ and we want to maximize the probability of observing the training set:  
 
<img src="https://github.com/user-attachments/assets/400ff4dd-a520-4d26-8ba8-b7e242a28856" width="450">

As we can see, if the predicted label is correct the penalty is 0 and increases quickly when the model is confident but wrong.
For only applying the loss function to the correct class probability we take the dot product between the log-probs vector and the one-hot encoded y.


### Optimization method
For optimizing our model we will use **stochastic gradient descent**, i.e. we choose randomly just one example for computing gradients.
To compute gradients we are using matrix derivatives with [denominator layout convention](https://introml.mit.edu/_static/fall23/LectureNotes/chapter_Matrix_derivative_common_cases.pdf) (to ensure that the gradient of the loss w.r.t. model's parameters
keep the same shape.)




### Modules
