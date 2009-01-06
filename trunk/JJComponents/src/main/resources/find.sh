#!/bin/sh
find . | grep -v '.svn' | grep -E '[^A-Za-z0-9\/\.\_\-]'
