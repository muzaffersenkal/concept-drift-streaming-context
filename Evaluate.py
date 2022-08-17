#!/usr/bin/env python
# coding: utf-8

# In[1]:


import pandas as pd
import numpy as np
import datetime, time
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
from matplotlib import gridspec
import random


# In[2]:


def get_unix(latency):
    millisecond = datetime.datetime.now() - datetime.timedelta(seconds=latency)
    unixtimestamp = int(millisecond.timestamp() * 1000)
    return unixtimestamp


# In[3]:


def plot_data(df, driftsExtra=None):
    fig = plt.figure(figsize=(7,3), tight_layout=True)
    gs = gridspec.GridSpec(1, 2, width_ratios=[3, 1])
    ax1 = plt.subplot(gs[0])
    ax1.grid()
    ax1.plot(df["latency"], label='Stream')
    drifts= list(df.query("drift == 1").index)
    if len(drifts) > 0:
        for i in drifts:
            ax1.axvline(i, color='red')
    if driftsExtra:
        for i in driftsExtra:
            ax1.axvline(i, color='yellow')
    
    plt.show()


# In[4]:


df = pd.read_csv("Data/data_drift_20.csv",names={'ingestion': pd.Series(dtype='int'),'event': pd.Series(dtype='int'),
                   'latency': pd.Series(dtype='float'),
                   'drift': pd.Series(dtype='int')})


# In[ ]:





# In[14]:


def calc_metric(driftTime, detectTime, threshold, false_alarm_threshold=5):
    
    tsDrift = datetime.datetime.fromtimestamp(int(driftTime)/1000)  
    tsDetect = datetime.datetime.fromtimestamp(int(detectTime)/1000)  
    diff = tsDetect - tsDrift
    diff = diff.total_seconds()
    if diff > 0 and diff <= threshold:
        return "TP"
    elif diff > 0 and diff> threshold and diff<false_alarm_threshold:
        return "FN"
    return "FP"
    



# In[26]:


def evaluate(threshold, df_p):
    TOTAL_DRIFT= 20
    detectedTS = df_p["ingestion"].tolist()
    driftsTS = df.query("drift == 1")["ingestion"].tolist()

    result= dict()
    result["name"] =  df_p["algorithm"].unique()[0]
    result["predictedDrift"] = len(detectedTS)
    result["TP"]=0 # should be drift  and it is in threshold
    result["FN"]=0 # should be drift it is not in threshold
    result["FP"]=0 # should not be drift (false alarm)
   
    
    
    
    for index,ingestion in enumerate(driftsTS):
        total_drift = len(driftsTS)
        
        first_drift = driftsTS[index]
        if index < total_drift-1:
            second_drift = driftsTS[index+1]
        else:
            second_drift = 9999999999999
        first_accepted = False # it can be 2 predictions within a period
        for j in detectedTS:
            if j > first_drift and j < second_drift:
                metric = calc_metric(ingestion, j, threshold)
                
                if metric == "TP":
                    result["TP"] += 1
                    first_accepted = True
                else:
                    if metric == "FP":
                        result["FP"] += 1
                    elif first_accepted:
                        result["FP"] += 1
                    elif metric == "FN":
                        result["FN"] += 1
                    else:
                        print("NA")
        

    
    return result


# In[27]:


files = ["result_sudden_ADWIN.csv","result_sudden_GMADM.csv", "result_sudden_CUSUM.csv", "result_sudden_PageHinkley.csv"]


# In[28]:


thresholds = [0.50,0.75, 1.25, 1.75]
df_result = pd.DataFrame({'algorithm': pd.Series(dtype='str'),'threshold': pd.Series(dtype='int'),
                   'predictedDrift': pd.Series(dtype='int'),
                          'TP': pd.Series(dtype='int'),'FP': pd.Series(dtype='int'),
                          'FN': pd.Series(dtype='float'),
                           'Sensitivity': pd.Series(dtype='float'),
                         'Precision': pd.Series(dtype='float'),})


for f in files:
    df_processed = pd.read_csv(f"Result/{f}",names={'ingestion': pd.Series(dtype='int'),'event': pd.Series(dtype='int'),
                   'indexNumber': pd.Series(dtype='int'),
                   'algorithm': pd.Series(dtype='str')})
    
    
    for t in thresholds:
        result = evaluate(t, df_processed)
        try:
            precision = result["TP"]/ ( result["TP"] + result["FP"])
        except:
            precision: 0
        df_result = df_result.append({"algorithm":result["name"] ,"threshold": t, "TP": result["TP"],
                                      "FP":result["FP"],  "FN": result["FN"],
                                      "predictedDrift": result["predictedDrift"],
                                     "Sensitivity": result["TP"]/ (result["TP"] + result["FN"]),
                                     "Precision": precision, },ignore_index=True) 


# In[29]:


df_result.to_csv("Result/evaluation_result.csv",index=False)




