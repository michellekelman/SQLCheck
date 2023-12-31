# SQLCheck
CS 6360 Semester Project
## Team Members:
- Safia Sharif
- Michelle Kelman
- Chris Fan
- Ruthwik Raj Chekkala
## Reference Paper:
https://dl.acm.org/doi/10.1145/3318464.3389754
## Original Implementation:
https://github.com/jarulraj/sqlcheck
## Dataset:
https://www.kaggle.com/datasets/thedevastator/understanding-contextual-questions-answers <br>
`SQL_Dataset_Examples.csv` is derived from the 80 longest select statements and 20 longest create statements in the dataset above.
## How to Install and Run SQLCheck:
1. Clone the repository <br>
```git clone https://github.com/michellekelman/SQLCheck.git```
2. (Optional) Add additional datasets to the repository in the form of `.csv` files with SQL query entries
3. Run the application with the default dataset `SQL_Dataset_Examples.csv` or a custom dataset <br>
```java main -f <dataset-file.csv>``` <br>
Example: ```java main -f SQL_Dataset_Examples.csv```
