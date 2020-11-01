import os
import sys


def parse_file(filename):
    with open(filename, 'r') as infile:
        asm_lines = []
        for line in infile:
            # skip empty lines
            if len(line.split()) == 0:
                continue
            else:
                asm_lines.append(line)


def check_input(file_path):
    # checks if the path given exists
    if os.path.exists(file_path):
        # checks if the path leads to a file
        if os.path.isfile(file_path):
            parse_file(file_path)
        # checks if the path leads to a directory
        elif os.path.isdir(file_path):
            files = os.listdir(file_path)
            # parse each file in the directory
            for file in files:
                parse_file(file)


def main():
    if len(sys.argv) == 2:
        file_path = sys.argv[1]
        check_input(file_path)


if __name__ == '__main__':
    main()
