## About The Project

Numerous data-driven systems stream massive amounts of data at a high rate as a result of technological advancements.  However, the data can change at any time, causing malfunctions and erroneous results. This is known as concept drift. These systems must be able to detect these changes. This research focuses on investigating operating conditions in streaming contexts in the presence of concept drift. Most common unsupervised concept drift algorithms were implemented on Apache Flink job and tried to detect concept drifts. 



## Getting Started

This is an example of how you may give instructions on setting up your project locally. To get a local copy up and running follow these simple example steps.


### Prerequisites

This is an example of how to list things you need to use the software and how to install them.

-   Java 

-   Apache Flink

-   Python

-   Jupyter Notebook

### Installation

1.  Clone the repo

``` sh
git clone https://github.com/muzaffersenkal/concept-drift-streaming-context
```
2. Install Python

3. Install Apache Flink

## How to run the Experiment

1. Provide Data

We have a script to generate drift data. You can run the script to get the data.

``` sh
python GenerateDriftData.py 
```

2. Run the Flink Job

```sh
$FLINK_PATH/bin/flink run --jarfile ./conceptdrift/jarfiles/DriftDetectJob.jar --algorithm ADWIN --output Result/ --input Data/data_drift_20.csv
```

##### Parameters for DriftDetectJob

**--algorithm:** drift detector algorithm
- ADWIN
- CUSUM
- GMADM
- PageHinkley

**--output:** result output folder

**--input:** drift data



## How to run DriftViewer

1.  Run the DriftViewer ( FastAPI )

``` sh
cd driftviewer
uvicorn main:app --reload
```

2. Run the Flink Job for DriftViewer

```sh
$FLINK_PATH/bin/flink run --jarfile ./conceptdrift/jarfiles/DriftDetectContinuousJob.jar --algorithm ADWIN 
```

3. Then go to http://localhost:8000 on your browser


## How to show the Experiment results

After run the experiment,

1. Run the Evaluation Python Script

``` sh
python Evaluate.py
```

The script will export a csv file to Result folder 




## Contributing

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request

## Contact

Muzaffer Senkal - [email](mailto:mzffersenkal@gmail.com)

Project Link: <https://github.com/muzaffersenkal/concept-drift-streaming-context/>
