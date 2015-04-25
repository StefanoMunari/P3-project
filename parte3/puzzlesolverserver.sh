#! /bin/bash

#run the binaries compiled previously with one parameter to main
cd bin
rmiregistry &
java server.PuzzleSolverServer $@