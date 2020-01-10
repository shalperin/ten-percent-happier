This is a Python3 Flask app designed to run on Google App Engine

setup
=====
$ python3 -m venv /path/to/venv
$ source /path/to/venv/bin/activate
$ pip install -r requirements.txt

run
===
$ python3 main.py

tear down
=========
(venv) MY MACHNE$ deactivate

deployment setup on GAE
=======================
The steps are roughly:
+ Install the Google Cloud SDK, Google App Engine, and Standard Python Environment
+ create project in Google Cloud console
+ gcloud init
(see: https://cloud.google.com/appengine/docs/standard/python3/quickstart)

deploy
======
gcloud app deploy
(see link above in deployment setup)



