
# echo-server.py

import socket
import time
import numpy as np
import datetime, time


# Generate data for 3 distributions
random_state = np.random.RandomState(seed=42)
dist_a = random_state.normal(2.8, 0.5, 1000)
dist_b = random_state.normal(2.4, 0.2, 1000)
dist_c = random_state.normal(2.7, 0.8, 1000)

# Concatenate data to simulate a data stream with 2 drifts
stream = np.concatenate((dist_a, dist_b, dist_c))

stream = iter(map(lambda x: {"value":float(x)},stream))


HOST = "127.0.0.1"  # Standard loopback interface address (localhost)
PORT = 8001  # Port to listen on (non-privileged ports are > 1023)

s =  socket.socket(socket.AF_INET, socket.SOCK_STREAM)

s.bind((HOST, PORT))
s.listen(5)

DATA_FLOW_STARTED = False

while True:
    conn, addr = s.accept()
    print(f"Connected by {addr}")

    while True:
        time.sleep(3)
        payload = next(stream)
        millisecond = datetime.datetime.now()
        unixtimestamp = int(time.mktime(millisecond.timetuple()) * 1000)
        message = str(unixtimestamp)+","+str(payload['value'])+ "\n"
        conn.sendall(message.encode())