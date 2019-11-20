#!/bin/bash

MAT="$1"
PROGRAM="P11_${MAT}"
echo "Test 1"
cmd='./${PROGRAM}'
eval "$cmd"


echo "\nTest 2"
eval "./${PROGRAM} al911911 A1114143 A1703280 A1701354 ltrejo"


./P12_570654 al911911 A1114143 A1703280 A1701354 ltrejo
