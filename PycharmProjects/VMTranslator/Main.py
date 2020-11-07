import os
import sys

# add methods that do:
# strip leading and trailing whitespaces


# def check_input(file_path):
#     """
#     checks the given file argument, to see if it exists, is a file or directory and calls the functions to
#     handle the input
#     @param file_path: file argument from command line
#     """
#     # checks if the path given exists
#     if os.path.exists(file_path):
#         assembler = Assembler()
#         # checks if the path leads to a file
#         if os.path.isfile(file_path) and file_path.lower().endswith(".vm"):
#             translated = parse_file(assembler, file_path)
#             dir_path = file_path.split(".")[0]
#             create_output_file(dir_path, translated)
#         # checks if the path leads to a directory
#         elif os.path.isdir(file_path):
#             files = os.listdir(file_path)
#             # parse each file in the directory
#             for file in files:
#                 if file.lower().endswith(".vm"):
#                     translated = parse_file(assembler, os.path.abspath(os.path.join(file_path, file)))
#                     create_output_file(os.path.join(file_path, file).split(".")[0], translated)
#                     assembler = Assembler()


def main():
    """
    main function of the program
    """
    if len(sys.argv) == 2:
        file_path = sys.argv[1]
        # check_input(file_path)


if __name__ == '__main__':
    main()
