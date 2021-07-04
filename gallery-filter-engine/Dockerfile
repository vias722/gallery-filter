FROM python:3.9.6-slim
COPY requirements.txt /
RUN apt install -y g++
RUN pip install -r requirements.txt
COPY gallery-filter-engine /gallery-filter-engine
COPY resources /resources
ENV FLASK_APP=gallery-filter-engine/main.py
ENTRYPOINT [ "flask", "run", "--host", "0.0.0.0"]