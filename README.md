# Neural-Network-Genetic-Algorithm

## Implementation of:
+ Forward Neural Network
+ Genetic algorithm

## Supervised Learning, Regression Problem
* Input:
```
x,y
3.469,-0.795
1.626,0.971
```

* Output:
```
[Train error @2000]: 0.002901
[Train error @4000]: 0.002151
[Train error @6000]: 0.001792
[Train error @8000]: 0.001636
[Train error @10000]: 0.001443
[Test error]: 0.000922
```

## Arguments
* Arguments from command line:
```
Path to the training dataset (--train)
Path to the test dataset (--test)
The neural network architecture (--nn)
The population size of the genetic algorithm (--popsize)
The elitism of the genetic algorithm (--elitism)
The mutation probability of each chromosome element (--p)
The standard deviation of the mutation Gaussian noise (--K)
The number of genetic algorithm iterations (--iter)
```

## Example
```
java -cp target/classes ui.Solution --train sine_train.txt --test
sine_test.txt --nn 5s --popsize 10 --elitism 1 --p 0.1 --K 0.1 --iter 10000
```
```
[Train error @2000]: 0.002106
[Train error @4000]: 0.001565
[Train error @6000]: 0.001097
[Train error @8000]: 0.000891
[Train error @10000]: 0.000830
[Test error]: 0.000433
```
