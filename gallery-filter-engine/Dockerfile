FROM python:3.9.6-slim
COPY requirements.txt /
RUN apt update && apt install -y g++
RUN pip install -r requirements.txt
COPY app /app
COPY resources /resources
ENTRYPOINT [ "flask", "run", "--host", "0.0.0.0"]