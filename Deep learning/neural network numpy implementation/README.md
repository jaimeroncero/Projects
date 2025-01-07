# Neural network numpy implementation
Design of a 2-layer fully connected artificial neural network using numpy. 

## Contents
- [Model architecture](model-architecture)
- [Loss function](loss_function)
- [Optimization method](optimization_method)
- [Modules](modules)
- [Final result](final_result)


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
keep the same shape.) For example $(x,y)$:  


- $$\frac{\partial L}{\partial s}$$ is a matrix $C \times 1$. We compute gradient of the loss w.r.t $s$ instead of $h$
  because this allow us to do some simplifications that increases numerical stability:  
  
 $$L = \-log(P(Y=y|x)) = -\log(\frac{e^{s_y}}{\sum_j{e^{s_j}}}) = -y + \log(\sum_j{e^{s_j}}) $$  
  
 $$(\frac{\partial L}{\partial s})_j = \frac{\partial L}{\partial s_j} =
  \frac{\partial}{\partial s_j}[ -y + \log(\sum_k{e^{s_k}})] =
  \frac{e^{s_j}}{\sum_k{e^{s_k}}} - \mathbb{1}\[y=j\]=
  softmax(s)_j - \mathbb{1}\[y=j\]
  \Rightarrow \frac{\partial L}{\partial s} = h_1 - yoh$$  
  
- The gradient $$\frac{\partial s}{\partial W}$$ is formally a 3D tensor:
    
 $$\frac{\partial  s}{\partial W_{i,j}} = (\cdots \frac{\partial s_k}{\partial W_{i,j}} \cdots )$$

 $$\frac{\partial s_k}{\partial W_{i,j}} = \frac{\partial}{\partial W_{i,j}}\[W_k \cdot h + b_k \] = \frac{\partial}{\partial W_{i,j}}\[\sum_{z}    W_{k,z} \cdot h_z \] = h_j \space \text{if  (i,j) = (k,z)}$$  

 $\frac{\partial s}{\partial W_{i,j}}$ is a vector whose i-th element is $h_j$ and zero elsewhere. We can flatten this out into the scalar $h_j$. Now $\frac{\partial s}{\partial W}$ is a matrix whose elements in j-th column are $h_j$. Stacking this we obtain:  

 $$\frac{\partial s}{\partial W} = h^T$$  

 - $\frac{\partial s}{\partial b}$ is a square matrix:

$$\frac{\partial s_j}{\partial b_i} = 1 \space \space \text{if i = j} \space \space \Rightarrow \frac{\partial s}{\partial b} = I$$  

- $\frac{\partial s}{\partial h}$ is a matrix $len(h) \times len(s)$:

$$\frac{\partial s_j}{\partial h_i} = \frac{\partial}{\partial h_i}W_j \cdot h = W_{j,i} \space \Rightarrow \space \space \frac{\partial s}{\partial b} = W^T$$  

- If $h = ReLU(s) = max(s,0)$ then $\frac{\partial h}{\partial s}$ is a square matrix. As ReLU function is applied element-wise this matrix will have zeros outside the diagonal. Diagonal's i-th element will be 1 if $s_i > 0$ or $0$ otherwise:

$$\frac{\partial ReLU(s)}{\partial s} = diag(1\[s > 0\])$$  

Now we can compute all the remaining gradients through backpropagation:  

$$\frac{\partial L}{\partial W_1} = \frac{\partial L}{\partial s_1} \cdot \frac{\partial s_1}{\partial W_1}$$  

$$\frac{\partial L}{\partial b_1} = \frac{\partial L}{\partial s_1} \cdot \frac{\partial s_1}{\partial b_1}$$    

$$\frac{\partial L}{\partial W_0} = \frac{\partial L}{\partial s_1} \cdot \frac{\partial s_1}{\partial h_0}
\cdot \frac{\partial h_0}{\partial s_0} \cdot \frac{\partial s_0}{\partial W_0}$$    

$$\frac{\partial L}{\partial b_0} = \frac{\partial L}{\partial s_1} \cdot \frac{\partial s_1}{\partial h_0}
\cdot \frac{\partial h_0}{\partial s_0} \cdot \frac{\partial s_0}{\partial b_0}$$ 
  



### Modules
|   |   |
|:--------|:-----------------|
| Data | Contains functions to generate sample data, display results and evaluate model's performance. |
| fcann2 | Implementation and training of the neural network using SGD. |
| pt_deep | Equivalent implementation using PyTorch to compare performance. |



### Final result

Data whose true label is 0 appears in white, 1 in grey. Model's predicted 0 region is yellow and 1 region is purple. Misclassified data points are those square shaped.
![image](https://github.com/user-attachments/assets/6e5c0090-0d96-425e-b452-a4cc348dcf9a)

