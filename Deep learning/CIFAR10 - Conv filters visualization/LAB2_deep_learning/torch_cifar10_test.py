import torch
import torch.nn as nn
import torchvision
import torchvision.transforms as transforms
from matplotlib import pyplot as plt
import os
import pickle
import numpy as np


# model definition

class CIFAR10Model(nn.Module):
    
    def __init__(self):
        super().__init__()
        # 3x32x32
        self.conv1 = nn.Conv2d(in_channels=3, out_channels=16, kernel_size=5)
        # 16x28x28
        self.relu1 = nn.ReLU()
        # 16x28x28
        self.pool1 = nn.MaxPool2d(kernel_size=3, stride=2)
        # 16x13x13
        self.conv2 = nn.Conv2d(16, 32, kernel_size=5)
        # 32 x 9 x 9
        self.relu2 = nn.ReLU()
        # 32 x 9 x 9
        self.pool2 = nn.MaxPool2d(kernel_size=3, stride=2)
        # 32 x 4 x 4
        self.flatten = nn.Flatten()
        # 32 * 4 * 4
        self.fc1 = nn.Linear(32 * 4 * 4, 216)
        self.relu3 = nn.ReLU()
        self.fc2 = nn.Linear(216, 128)
        self.relu4 = nn.ReLU()
        self.fc3 = nn.Linear(128, 10)

    def forward(self, x):
        x = self.conv1(x)
        x = self.relu1(x)
        x = self.pool1(x)

        x = self.conv2(x)
        x = self.relu2(x)
        x = self.pool2(x)

        x = self.flatten(x)

        x = self.fc1(x)
        x = self.relu3(x)

        x = self.fc2(x)
        x = self.relu4(x)

        x = self.fc3(x)

        return x



model = CIFAR10Model()
model.load_state_dict(torch.load('cifar10model.pth'))

transform = transforms.Compose([transforms.ToTensor()])
testset = torchvision.datasets.CIFAR10(root='./datasets/CIFAR10', train=False, download=True, transform=transform)
testloader = torch.utils.data.DataLoader(testset, batch_size=1, shuffle=False)

criterion = nn.CrossEntropyLoss()
    

        
top_losses = []

for batch_x, batch_y in testloader:
    with torch.no_grad():
        outputs = model(batch_x)
        loss = criterion(outputs, batch_y)  # Compute per-element loss
        top_losses.append((loss, batch_x[0], batch_y[0]))

# Sort by loss in descending order and take the top 20
top_losses = sorted(top_losses, key = lambda x : x[0], reverse=True)[:20]

# Visualize the top 20 elements with the highest loss
fig, axes = plt.subplots(5, 4, figsize=(15, 10))
for i, (loss, image, label) in enumerate(top_losses):
    ax = axes[i // 4, i % 4]
    ax.imshow(image.permute(1, 2, 0).numpy())
    ax.set_title(f"Label: {label}\nLoss: {loss:.4f}")
    ax.axis('off')

plt.tight_layout()
plt.show()




        



