#!/bin/bash

mkdir -p outputs; find inputs -type d -exec mkdir -p outputs/{} \;
find inputs/* | grep [.] | (cat - && echo "exit") | java LSBE

