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

2.  Generate Drift Data

3.  Run Flink Job (DriftDetectJob)

## data

# How to run the Experiment

1. Generate Artificial Drift Data

    ``` sh
    python generate_drift_data.py
    ```

2. Run the Flink Job

    ``` sh
    ..
    ```

# How to run DriftViewer

1.  Run the backend server ( FastAPI )

    ``` sh
    uvicorn server.main:app --reload
    ```

2. Run the Flink Job for DriftViewer

    ``` sh
    ..
    ```



## How to show the Experiment results

After run the experiment,

1. Run the Evaluation Python Script

    ``` sh
    python evaluate.py
    ```




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
