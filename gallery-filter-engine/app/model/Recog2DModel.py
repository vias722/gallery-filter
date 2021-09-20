import os, io, pathlib, requests

import torch
import torch.nn as nn
import torch.nn.functional as F

import pytorch_lightning as pl

from torchvision import transforms, datasets
from torchvision import models
from PIL import ImageFile, Image
ImageFile.LOAD_TRUNCATED_IMAGES = True

class Net(pl.LightningModule):

    def __init__(self, batch_size=256, num_workers=8):
        super(Net, self).__init__()

        # バッチサイズ
        self.batch_size = batch_size

        # Data Loader で使用するデータをロードするコア数
        self.num_workers = num_workers
        
        self.conv = models.resnet18(pretrained=True)
        self.fc = nn.Linear(1000, 2)
        
    def lossfun(self, y, t):
        return F.cross_entropy(y, t)

    def configure_optimizers(self):
        return torch.optim.SGD(self.parameters(), lr=0.01)

    def forward(self, x):
        x = self.conv(x)
        x = self.fc(x)

        return x


class RecogModel:
    def __init__(self) -> None:
        self.model = Net()
        self.model.load_state_dict(torch.load('resources/2d-recog.pt', map_location=torch.device('cpu')))
        self.model.eval()

        self.transform = transforms.Compose([
            transforms.Resize([256, 256]),
            transforms.ToTensor(),
            transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
        ])

    def readPicture(self, image_file):
        urlData = requests.get(image_file).content
        image = Image.open(io.BytesIO(urlData))
        image = image.convert('RGB')
        image =self.transform(image)
        
        return image

    def detectImage(self, file):
        file = str(file)
        
        img = self.readPicture(file)
        result = self.model.forward(img.view(1,3,256,256))[0]
        return result

    def detectImages(self, files):
        imgs = []
        for file in files:
            file = str(file)
            imgs.append(torch.tensor(self.readPicture(file)))
        
        imgs_tensor = torch.stack(imgs, 0)

        result = self.model.forward(imgs_tensor)
        return result

    def recognize(self, images):
        # images = pathlib.Path(path).glob('*')
        list = []
        result_list = []
        count = 0
        # for image in images:
        #     #list.append(image)
        #     result = self.detectImage(image)
        #     hash = {}
        #     hash['url'] = image
        #     # if result != None:
        #     #     result_list.append(image)
        #     hash['result'] =  '2d' if result[0]<result[1] else '3d'
        #     result_list.append(hash)

        print('detecting')
        for result in self.detectImages(images):
            
            hash = {}
            hash['url'] = images[count]
            # if result != None:
            #     result_list.append(image)
            hash['result'] =  '2d' if result[0]<result[1] else '3d'
            result_list.append(hash)
            count += 1

        print('finish')
        return result_list

