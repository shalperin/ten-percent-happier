from flask import Flask, Response, request, jsonify, make_response

import json

app = Flask(__name__)

@app.route('/')
def hello_world():
    return 'Hello, World!'

@app.route('/add', methods=["POST"])
def add():
    a = 0
    b = 0
    emsg=""
    try :
        a = float(request.form['a'])
        b = float(request.form['b'])
    except Exception as e:
        emsg = str(e)
        
    if emsg != "":
       return make_response(jsonify({"exception": emsg}), 400)
    else:
        return jsonify({"sum" : a+b})
