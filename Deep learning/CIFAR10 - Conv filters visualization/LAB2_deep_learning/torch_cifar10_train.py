import torch
import torch.nn as nn
import torch.optim as optim
import torchvision
from torchvision import transforms
from matplotlib import pyplot as plt
import os
import numpy as np
import math
from sklearn.metrics import accuracy_score

transform = transforms.Compose([transforms.ToTensor()])

trainset = torchvision.datasets.CIFAR10(root='./datasets/CIFAR10', train=True, download=True, transform=transform)
testset = torchvision.datasets.CIFAR10(root='./datasets/CIFAR10', train=False, download=True, transform=transform)

config = {
    'batch_size': 32,
    'weight_decay': 1e-4,
    'lr': 1e-3,
    'n_epochs': 50,
}

trainloader = torch.utils.data.DataLoader(trainset, batch_size=config['batch_size'], shuffle=True)
testloader = torch.utils.data.DataLoader(testset, batch_size=config['batch_size'], shuffle=False)


class CIFAR10Model(nn.Module):
    def __init__(self):
        super().__init__()
        self.conv1 = nn.Conv2d(in_channels=3, out_channels=16, kernel_size=5)
        self.relu1 = nn.ReLU()
        self.pool1 = nn.MaxPool2d(kernel_size=3, stride=2)
        self.conv2 = nn.Conv2d(16, 32, kernel_size=5)
        self.relu2 = nn.ReLU()
        self.pool2 = nn.MaxPool2d(kernel_size=3, stride=2)
        self.flatten = nn.Flatten()
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


def evaluate(model, data_loader, criterion, device):
    model.eval()
    total_loss = 0.0
    all_preds = []
    all_labels = []

    with torch.no_grad():
        for inputs, labels in data_loader:
            inputs, labels = inputs.to(device), labels.to(device)
            outputs = model(inputs)
            loss = criterion(outputs, labels)
            total_loss += loss.item()

            _, preds = torch.max(outputs, 1)
            all_preds.extend(preds.cpu().numpy())
            all_labels.extend(labels.cpu().numpy())

    accuracy = accuracy_score(all_labels, all_preds)
    return total_loss / len(data_loader), accuracy


def plot_training_progress(save_dir, data):
  fig, ((ax1, ax2), (ax3, ax4)) = plt.subplots(2, 2, figsize=(16,8))

  linewidth = 2
  legend_size = 10
  train_color = 'm'
  val_color = 'c'

  num_points = len(data['train_loss'])
  x_data = np.linspace(1, num_points, num_points)
  ax1.set_title('Cross-entropy loss')
  ax1.plot(x_data, data['train_loss'], marker='o', color=train_color,
           linewidth=linewidth, linestyle='-', label='train')
  ax1.plot(x_data, data['valid_loss'], marker='o', color=val_color,
           linewidth=linewidth, linestyle='-', label='validation')
  ax1.legend(loc='upper right', fontsize=legend_size)
  ax2.set_title('Average class accuracy')
  ax2.plot(x_data, data['train_acc'], marker='o', color=train_color,
           linewidth=linewidth, linestyle='-', label='train')
  ax2.plot(x_data, data['valid_acc'], marker='o', color=val_color,
           linewidth=linewidth, linestyle='-', label='validation')
  ax2.legend(loc='upper left', fontsize=legend_size)
  ax3.set_title('Learning rate')
  ax3.plot(x_data, data['lr'], marker='o', color=train_color,
           linewidth=linewidth, linestyle='-', label='learning_rate')
  ax3.legend(loc='upper left', fontsize=legend_size)

  save_path = os.path.join(save_dir, 'training_plot.png')
  print('Plotting in: ', save_path)
  plt.savefig(save_path)

device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
model = CIFAR10Model().to(device)
criterion = nn.CrossEntropyLoss()
optimizer = optim.Adam(model.parameters(), lr=config['lr'], weight_decay=config['weight_decay'])
lr_scheduler = optim.lr_scheduler.StepLR(optimizer, step_size=10, gamma=0.1)

plot_data = {'train_loss': [], 'valid_loss': [], 'train_acc': [], 'valid_acc': [], 'lr': []}

for epoch in range(config['n_epochs']):
    model.train()
    epoch_loss = 0.0
    for batch_x, batch_y in trainloader:
        batch_x, batch_y = batch_x.to(device), batch_y.to(device)

        optimizer.zero_grad()
        outputs = model(batch_x)
        loss = criterion(outputs, batch_y)
        loss.backward()
        optimizer.step()
        epoch_loss += loss.item()

    train_loss, train_acc = evaluate(model, trainloader, criterion, device)
    valid_loss, valid_acc = evaluate(model, testloader, criterion, device)

    plot_data['train_loss'].append(train_loss)
    plot_data['valid_loss'].append(valid_loss)
    plot_data['train_acc'].append(train_acc)
    plot_data['valid_acc'].append(valid_acc)
    plot_data['lr'].append(optimizer.param_groups[0]['lr'])

    print(f"Epoch {epoch + 1}/{config['n_epochs']}, Train Loss: {train_loss:.4f}, Train Acc: {train_acc:.4f}, "
          f"Valid Loss: {valid_loss:.4f}, Valid Acc: {valid_acc:.4f}")

    lr_scheduler.step()

SAVE_DIR = './CIFAR10_out'
os.makedirs(SAVE_DIR, exist_ok=True)
plot_training_progress(SAVE_DIR, plot_data)
torch.save(model.state_dict(), "cifar10model.pth")
