import json
from model import Recog2DModel

from flask import Flask, request


app = Flask(__name__)
model = Recog2DModel.RecogModel()



@app.route("/", methods=['POST'])
def recongnize():
    urls = json.loads(request.data)
    return json.dumps(model.recognize(urls))