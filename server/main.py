import json
import asyncio
from fastapi import FastAPI
from fastapi import Request
from fastapi import WebSocket
from fastapi.templating import Jinja2Templates
from fastapi.staticfiles import StaticFiles
import numpy as np
import datetime, time
from fastapi import BackgroundTasks
import asyncio
from pydantic import BaseModel
import uuid
from typing import Optional

STREAM_STARTED = False
NEW_DRIFT = False
DRIFTS = []
DIST_MEAN = 2.8
DIST_SD = 0.5

# Generate data for 3 distributions
random_state = np.random.RandomState(seed=42)




class Drift(BaseModel):
    id: Optional[str] = uuid.uuid4().hex
    type: str
    eventTime: int
    processTime: int


app = FastAPI()
templates = Jinja2Templates(directory="templates")
app.mount("/static", StaticFiles(directory="static"), name="static")

# read manually
with open('measurements.json', 'r') as file:
    measurements = iter(json.loads(file.read()))

@app.get("/")
def read_root(request: Request):
    return templates.TemplateResponse("index.html", {"request": request})

@app.get("/start")
def read_root(request: Request):
    STREAM_STARTED = True
    return "OK"

@app.post("/drift")
async def create_drift(drift: Drift):
    print("request alındı")
    global NEW_DRIFT
    NEW_DRIFT = True
    DRIFTS.append(drift)
    return drift

@app.get("/drift")
def get_drift(request: Request):
    global DRIFTS
    return DRIFTS

@app.get("/stop")
def read_root(request: Request):
    STREAM_STARTED = False
    return "OK"

"""_summary_
SENDING Data:
    type: 'data'
    eventTime: 1233

SENDING Drift:
    type: 'drift'
    drifts: []
        - id
        - type
        - eventTime

RECEIVE Conf
    type: 'conf'
    config:
        'delay': 12

"""

@app.websocket("/ws")
async def websocket_endpoint(websocket: WebSocket):
    global stream, STREAM_STARTED, NEW_DRIFT, DIST_MEAN , DIST_SD
    await websocket.accept()
    while True:
        resp = await websocket.receive_text()
        resp = json.loads(resp)
        if resp["type"] == "conf":
            print(resp)
            DIST_MEAN =  float(resp["distmean"])
            DIST_SD = float(resp["distsd"])
            ##stream = iter(map(lambda x: {"value":float(x)}, dist_c))
        payload = dict()
        payload["value"] = random_state.normal(DIST_MEAN, DIST_SD)
        payload["type"] = "data"
        file_path = '../conceptdrift/Data/output.csv' #choose your file path
        with open(file_path, "a") as output_file:
            millisecond = datetime.datetime.now()
            unixtimestamp = int(time.mktime(millisecond.timetuple()) * 1000)
            output_file.write(str(unixtimestamp)+","+str(payload['value'])+ "\n")
        #if STREAM_STARTED:
        await websocket.send_json(payload)
        if NEW_DRIFT:
            driftPayload = dict()
            driftPayload["type"] = "drift"
            driftPayload["drifts"] = list(map(lambda x: x.dict(), DRIFTS))
            await websocket.send_json(driftPayload)
            NEW_DRIFT = False



