# InfoAL

## Motivation

This project provides a method for generating training data for entity resolution problems. The goal of entity resolution 
is to identify records representing the same real world entity. For instance, the identification of duplicates for two 
publication data sources shown below. Due to quality issues, similarity functions are utilized to compute similarities 
between the given property values for each record pair. The determined similarities can also be interpreted as probabilities
how likely it is that a record pair represent the same real world entity. However, the decision task using the similarities is challenging.
Therefore, machine learning techniques are used to determine classification models based on a labelled set of record pairs,
to classify pairs based on the similarity vectors as match or non-match.


### DBLP
| id | title | authors  | venue | year|
|---|------|------------|-------|------|
| 1 | Phoenix Project: Fault-Tolerant Applications | R Barga, D Lomet | SIGMOD Record | 2002|
| 2 |Mining the World Wide Web: An Information Search Approach - Book Review | N/A |N/A | 2002|
|3 | L/MRP: A Buffer Management Strategy for Interactive Continuous Data Flows in a Multimedia DBMS | F Moser, A Kraiss, W Klas | VLDB | 1995|

### Google Scholar
| id | title | authors  | venue | year|
|---|------|------------|-------|------|
|1 | Phoenix Project: Fault Tolerant Applications | D Lomet, R Barga | SIGMOD Record, | |
|2 | Mining the world wide web: an information search approach by George Chang, Marcus J. Healey (editor) | A Ouksel | ACM SIGMOD Record, | 2002|
|3| L/MRP: A Buffer Management Strategy for Interactive Continuous Data Flows in a Multimedia DBMS | F Moser, W Klas | PROCEEDINGS OF THE INTERNATIONAL CONFERENCE ON VERY LARGE  &hellip;,| 1995|

## Workflow

Input (simplified): 
 - similarity vectors where each vector represents a record pair (The similarities must already be computed.)
 - budget b number of labelled vectors
 - k number of vectors per iteration 
 
Output: 
- set of labelled similarity vectors being used for generating a classifier

## Using InfoAL
you can test the application in the console.

```
java de.uni_leipzig.dbs.entity_resolution.examples.machineLearning.active.InformativenessActiveLearning -i example_data/cora-weight_vectors.csv -g example_data/weight_vectors.csv -s Farthest_First
-b 200 -k 30 -a 0.5 -r cora_result.csv
```

Paramter | Description
---------|-------------
i | similarity vector file
g | gold standard with labelled vectors 
b | total number of records
k | number of records per iteration
a | weight of entropy and uncertainty
r | output file for the quality evaluation     
