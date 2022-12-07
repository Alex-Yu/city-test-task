# Test tasks
## Task 1
Implementation: **ParallelScan.scala**

Test coverage: **ParallelScanSpec.scala**

## Task 2
Scala meter benchmark implementation: **ScanBenchmark.scala** (under **test** folder).

HTML report: **benchmarks/report/index.html**

### Results:
- **array size: 10k**

Best sequential processing time: 0.03ms

Best parallel processing time: 0.03ms

Threshold: 2048

Max speedup: 0


- **array size: 20k**

Best sequential processing time: 0.06ms

Best parallel processing time: 0.04ms

Threshold: 2048

Max speedup: x1.5


- **array size: 50k**

Best sequential processing time: 0.15ms

Best parallel processing time: 0.05ms

Threshold: 2048

Max speedup: x3


- **array size: 100k**

Best sequential processing time: 0.3ms

Best parallel processing time: 0.07ms

Threshold: 4096

Max speedup: x4.3


- **array size: 1m**

Best sequential processing time: 3.07ms

Best parallel processing time: 0.29ms

Threshold: 16384

Max speedup: x10.6


- **array size: 10m**

Best sequential processing time: 30.59ms

Best parallel processing time: 3.44ms

Threshold: 32768

Max speedup: x8.9

### Conclusions for my system:
- most optimal option for the threshold is to set it based on the size of the input array
- best speedups could be achieved for input arrays of 1m+ elements
- recommended thresholds by array size (based on observations and could be further optimized):
  - <50k elements => 2048
  - 50..500k elements => 4096 
  - 500k..1m elements => 8192
  - 1m..5m elements => 16384
  - 5m..10m elements => 32768
- threshold of 8192 could be considered as the default one independent of the array size
(performance impact up to -20% compared to the size based threshold picking).


## Task 3
### General conclusions
- There is no point to parallelize small arrays because of parallelization costs. 
Depending on the system there should be some minimum array size for which 
the parallelization is starting to make sense. 
- The maximum speed-up would be always less than the number of cores
because of the parallelization costs.
- The best threshold could be taken based on the size of the array. 
With the growth of the array the optimal threshold is also growing in value and
could be approximately calculated by formula: size_of_array / cores.
- Taking too small threshold is bad because of the growing parallelization costs.
- Taking too big threshold can be as bad as taking it too small because of the
partial cores' utilization.
