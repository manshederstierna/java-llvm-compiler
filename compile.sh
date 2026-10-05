#!/usr/bin/env bash

source_file="$1"
source_file_length="${#source_file}"

if [[ ! -e "$source_file" ]] then
	echo "Source file not found: $source_file"
	exit 1
fi

if [[ "${source_file:source_file_length-4}" != ".cow" ]] 
then
	echo "Error: can only compile .cow files" >&2
	exit 1
fi


