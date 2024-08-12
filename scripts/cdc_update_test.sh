#!/bin/bash

declare base_dir="$(dirname $(dirname $(realpath $0)))"
cd ${base_dir}

echo MAKE SURE TO RUN ./gradlew clean benchmarkJar BEFORE RUNNING THIS SCRIPT

mkdir -p results

run_test() {
	rm binary_streams/cdc_input_binary
    ln -s binary_streams/cdc_input_binary binary_streams/$1
    java -jar build/libs/concurrent-dynamic-connectivity-1.0-SNAPSHOT-benchmark.jar
    mv results/update_dcp_results.csv results/$1_results.csv
}

# Test run
run_test "kron_13_query10_binary"

# # Full sweep
# run_test "kron_13_query10_binary"
# run_test "kron_15_query10_binary"
# run_test "kron_16_query10_binary"
# run_test "kron_17_query10_binary"
# # run_test "kron_18_query10_binary"

# run_test "dnc_query10_binary"
# run_test "tech_query10_binary"
# run_test "enron_query10_binary"

# run_test "twitter_query10_binary"
# run_test "stanford_query10_binary"
# run_test "random2N_query10_binary"
# run_test "randomNLOGN_query10_binary"
# run_test "randomNSQRTN_query10_binary"
# run_test "randomDIV_query10_binary"

# # Fixed-forest
# run_test "kron_13_ff_query10_binary"
# run_test "kron_15_ff_query10_binary"
# run_test "kron_16_ff_query10_binary"
# run_test "kron_17_ff_query10_binary"
# # run_test "kron_18_ff_query10_binary"

# run_test "dnc_ff_query10_binary"
# run_test "tech_ff_query10_binary"
# run_test "enron_ff_query10_binary"

# run_test "twitter_ff_query10_binary"
# run_test "stanford_ff_query10_binary"
# run_test "random2N_ff_query10_binary"
# run_test "randomNLOGN_ff_query10_binary"
# run_test "randomNSQRTN_ff_query10_binary"
# run_test "randomDIV_ff_query10_binary"
