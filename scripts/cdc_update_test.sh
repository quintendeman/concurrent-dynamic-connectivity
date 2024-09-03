#!/bin/bash

declare base_dir="$(dirname $(dirname $(realpath $0)))"
cd ${base_dir}

echo MAKE SURE TO RUN ./gradlew clean benchmarkJar BEFORE RUNNING THIS SCRIPT

mkdir -p results

run_test() {
	export CDC_INPUT_FILE=binary_streams/$1
    export CDC_OUTPUT_FILE=results/$1.csv
	cat $CDC_INPUT_FILE > /dev/null
    java -jar build/libs/concurrent-dynamic-connectivity-1.0-SNAPSHOT-benchmark.jar
}

declare -a streams=(
[0]="kron_13_query10_binary"
[1]="kron_15_query10_binary"
[2]="kron_16_query10_binary"
[3]="kron_17_query10_binary"
[4]="kron_18_query10_binary"
#
[5]="dnc_query10_binary"
[6]="tech_query10_binary"
[7]="enron_query10_binary"
#
[8]="twitter_query10_binary"
[9]="stanford_query10_binary"
[10]="random2N_query10_binary"
[11]="randomNLOGN_query10_binary"
[12]="randomNSQRTN_query10_binary"
[13]="randomDIV_query10_binary"
# Fixed Forest
[14]="kron_13_ff_query10_binary"
[15]="kron_15_ff_query10_binary"
[16]="kron_16_ff_query10_binary"
[17]="kron_17_ff_query10_binary"
[18]="kron_18_ff_query10_binary"
#
[19]="dnc_ff_query10_binary"
[20]="tech_ff_query10_binary"
[21]="enron_ff_query10_binary"
#
[22]="twitter_ff_query10_binary"
[23]="stanford_ff_query10_binary"
[24]="random2N_ff_query10_binary"
[25]="randomNLOGN_ff_query10_binary"
[26]="randomNSQRTN_ff_query10_binary"
[27]="randomDIV_ff_query10_binary"
)

run_test ${streams[$1]}
exit



# Full sweep
run_test "kron_13_query10_binary"
run_test "kron_15_query10_binary"
run_test "kron_16_query10_binary"
run_test "kron_17_query10_binary"
# run_test "kron_18_query10_binary"

run_test "dnc_query10_binary"
run_test "tech_query10_binary"
run_test "enron_query10_binary"

run_test "twitter_query10_binary"
run_test "stanford_query10_binary"
run_test "random2N_query10_binary"
run_test "randomNLOGN_query10_binary"
run_test "randomNSQRTN_query10_binary"
run_test "randomDIV_query10_binary"

# Fixed-forest
run_test "kron_13_ff_query10_binary"
run_test "kron_15_ff_query10_binary"
run_test "kron_16_ff_query10_binary"
run_test "kron_17_ff_query10_binary"
# run_test "kron_18_ff_query10_binary"

run_test "dnc_ff_query10_binary"
run_test "tech_ff_query10_binary"
run_test "enron_ff_query10_binary"

run_test "twitter_ff_query10_binary"
run_test "stanford_ff_query10_binary"
run_test "random2N_ff_query10_binary"
run_test "randomNLOGN_ff_query10_binary"
run_test "randomNSQRTN_ff_query10_binary"
run_test "randomDIV_ff_query10_binary"
