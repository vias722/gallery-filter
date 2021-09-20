import json
from . import model

from flask import Flask, request, jsonify


app = Flask(__name__)
model = model.RecogModel()



@app.route("/", methods=['POST'])
def recongnize():
    urls = json.loads(request.data)
    return jsonify(model.recognize(urls))