import time
from pathlib import Path
import numpy as np
import matplotlib.pyplot as plt
import torch
import torch.nn as nn
import torch.optim as optim
from torchvision.datasets import MNIST
from torch.utils.data import DataLoader, TensorDataset

DATA_DIR = Path(__file__).parent / 'datasets' / 'MNIST'
SAVE_DIR = Path(__file__).parent / 'out'

config = {}
config['max_epochs'] = 2
config['batch_size'] = 50
config['save_dir'] = SAVE_DIR
config['weight_decay'] = 1e-1
config['lr_policy'] = {1: {'lr': 1e-1}, 3: {'lr': 1e-2}, 5: {'lr': 1e-3}, 7: {'lr': 1e-4}}

# Function to convert dense labels to one-hot encoding
def dense_to_one_hot(y, class_count):
    return np.eye(class_count)[y]

np.random.seed(int(time.time() * 1e6) % 2**31)

ds_train, ds_test = MNIST(DATA_DIR, train=True, download=True), MNIST(DATA_DIR, train=False)
train_x = ds_train.data.unsqueeze(1).numpy().astype(float) / 255
train_y = ds_train.targets.numpy()
train_x, valid_x = train_x[:55000], train_x[55000:]
train_y, valid_y = train_y[:55000], train_y[55000:]
test_x = ds_test.data.unsqueeze(1).numpy().astype(float) / 255
test_y = ds_test.targets.numpy()

# Normalize data
train_mean = train_x.mean()
train_x, valid_x, test_x = (x - train_mean for x in (train_x, valid_x, test_x))
train_y, valid_y, test_y = (dense_to_one_hot(y, 10) for y in (train_y, valid_y, test_y))

# Convert data to PyTorch tensors and DataLoader
def create_dataloader(x, y, batch_size):
    dataset = TensorDataset(torch.tensor(x, dtype=torch.float32), torch.tensor(y, dtype=torch.float32))
    return DataLoader(dataset, batch_size=batch_size, shuffle=True)

train_loader = create_dataloader(train_x, train_y, config['batch_size'])
valid_loader = create_dataloader(valid_x, valid_y, config['batch_size'])
test_loader = create_dataloader(test_x, test_y, config['batch_size'])

class ConvNet(nn.Module):
    def __init__(self):
        super().__init__()
        self.conv1 = nn.Conv2d(1, 16, kernel_size=5, padding=2)
        self.pool1 = nn.MaxPool2d(kernel_size=2, stride=2)
        self.relu1 = nn.ReLU()
        self.conv2 = nn.Conv2d(16, 32, kernel_size=5, padding=2)
        self.pool2 = nn.MaxPool2d(kernel_size=2, stride=2)
        self.relu2 = nn.ReLU()
        self.flatten = nn.Flatten()
        self.fc1 = nn.Linear(32 * 7 * 7, 512)
        self.relu3 = nn.ReLU()
        self.fc2 = nn.Linear(512, 10)

    def forward(self, x):
        x = self.conv1(x)
        x = self.pool1(x)
        x = self.relu1(x)
        x = self.conv2(x)
        x = self.pool2(x)
        x = self.relu2(x)
        x = self.flatten(x)
        x = self.fc1(x)
        x = self.relu3(x)
        x = self.fc2(x)
        return x

model = ConvNet()
criterion = nn.CrossEntropyLoss()
optimizer = optim.Adam(model.parameters(), weight_decay=config['weight_decay'])

losses = []  # To track loss across epochs

# Training loop
for epoch in range(config['max_epochs']):
    model.train()
    epoch_loss = 0
    for batch_x, batch_y in train_loader:
        optimizer.zero_grad()
        outputs = model(batch_x)
        loss = criterion(outputs, torch.argmax(batch_y, dim=1))
        loss.backward()
        optimizer.step()
        epoch_loss += loss.item()

    # Average loss for the epoch
    avg_loss = epoch_loss / len(train_loader)
    losses.append(avg_loss)

    # Visualize filters after each epoch
    filters = model.conv1.weight.data.numpy()
    num_filters = filters.shape[0]
    fig, axes = plt.subplots(1, num_filters, figsize=(15, 5))
    for i, ax in enumerate(axes):
        ax.imshow(filters[i, 0], cmap='gray')
        ax.axis('off')
    plt.savefig(SAVE_DIR / f'filters_epoch_{epoch}.png')
    plt.close()

# Plot loss evolution
plt.plot(range(1, config['max_epochs'] + 1), losses, marker='o')
plt.title('Loss Evolution')
plt.xlabel('Epoch')
plt.ylabel('Loss')
plt.savefig(SAVE_DIR / 'loss_evolution.png')
plt.show()

# Evaluate on test data
model.eval()
test_loss = 0
correct = 0
with torch.no_grad():
    for batch_x, batch_y in test_loader:
        outputs = model(batch_x)
        test_loss += criterion(outputs, torch.argmax(batch_y, dim=1)).item()
        pred = outputs.argmax(dim=1, keepdim=True)
        correct += pred.eq(torch.argmax(batch_y, dim=1).view_as(pred)).sum().item()

# Average test loss and accuracy
avg_test_loss = test_loss / len(test_loader)
accuracy = correct / len(test_loader.dataset)
print(f'Test Loss: {avg_test_loss:.4f}, Accuracy: {accuracy:.2%}')
