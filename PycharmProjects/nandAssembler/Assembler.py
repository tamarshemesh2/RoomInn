import os
import re
import sys

from Parser import Parser
from SymbolTable import SymbolTable


class Assembler:
    def __init__(self):
        self.symbol_table = SymbolTable()

    def get_labels_and_vars(self, file_lines):
        for line in file_lines:
            # check if is variable i.e. @ then lowercase letters
            is_variable = re.match('@[a-z]+', line)
            is_label = re.match('[(][A-Z]+[)]', line)
            if bool(is_variable):
                # adds variable to the symbol table
                self.symbol_table.add_entry(line.split("@"), self.symbol_table.get_counter())
            elif bool(is_label):
                self.symbol_table.add_entry(re.sub('[()]', '', line), self.symbol_table.get_counter())

    def parse_instructions(self, instructions):
        parser = Parser(instructions)
        while parser.hasMoreCommands():
            current_parsed_instruction = []
            command_type = parser.commandType()
            if command_type == "C_COMMAND":
                current_parsed_instruction.append(parser.comp())
                current_parsed_instruction.append(parser.dest())
                current_parsed_instruction.append(parser.jump())
            parser.advance()


def parse_file(assembler, filename):
    with open(filename, 'r') as infile:
        asm_lines = []
        for line in infile:
            # skip empty lines
            if len(line.split()) == 0 or line.startswith("//"):
                continue
            else:
                asm_lines.append("".join(line.split()))
        assembler.get_labels_and_vars(asm_lines)
    infile.close()


def check_input(assembler, file_path):
    # checks if the path given exists
    if os.path.exists(file_path):
        # checks if the path leads to a file
        if os.path.isfile(file_path):
            parse_file(assembler, file_path)
        # checks if the path leads to a directory
        elif os.path.isdir(file_path):
            files = os.listdir(file_path)
            # parse each file in the directory
            for file in files:
                parse_file(assembler, os.path.abspath(os.path.join(file_path, file)))


def main():
    if len(sys.argv) == 2:
        file_path = sys.argv[1]
        assembler = Assembler()
        check_input(assembler, file_path)


if __name__ == '__main__':
    main()
