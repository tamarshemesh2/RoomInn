import os
import re
import sys

from Code import Code
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
        code = Code()
        translated = []
        while parser.hasMoreCommands():
            command_type = parser.commandType()
            # C instruction
            if command_type == "C_COMMAND":
                self.translate_c_instruction(parser, code, translated)
            # A instruction
            elif command_type == "A_COMMAND":
                self.translate_a_instruction(parser, translated)
            # shift command
            elif command_type == "S_COMMAND":
                self.translate_shift_command(parser, code, translated)
            parser.advance()
        return translated

    def translate_c_instruction(self, parser, code, translated):
        comp_field = code.comp(parser.comp())
        dest_field = code.dest(parser.dest())
        jump_field = code.jump(parser.jump())
        current_translated = "111" + str(comp_field[0]) + comp_field[1] + dest_field + jump_field
        translated.append(current_translated)

    def translate_a_instruction(self, parser, translated):
        parsed_symbol = parser.symbol()
        is_digit = re.match('[\\d]+', parsed_symbol)
        if is_digit:
            translated.append(str("{0:016b}".format(int(parsed_symbol))))
        else:
            if self.symbol_table.contains(parsed_symbol):
                translated.append(str("{0:016b}".format(self.symbol_table.get_address(parsed_symbol))))

    def translate_shift_command(self, parser, code, translated):
        shift_field = parser.shift()
        comp_field = parser.comp()
        dest_field = parser.dest()
        jump_field = parser.jump()
        current_translated = shift_field + code.comp(comp_field)[3:7] + code.dest(dest_field) + code.jump(jump_field)
        translated.append(current_translated)

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
        translated = assembler.parse_instructions(asm_lines)
    infile.close()
    return translated


def create_output_file(filename, translated_lines):
    output_filename = filename + ".hack"
    with open(output_filename, 'w') as outfile:
        for line in translated_lines:
            outfile.write("%s\n" % line)
    outfile.close()

def check_input(assembler, file_path):
    # checks if the path given exists
    if os.path.exists(file_path):
        # checks if the path leads to a file
        if os.path.isfile(file_path):
            translated = parse_file(assembler, file_path)
            create_output_file(os.path.splitext(file_path)[0], translated)
        # checks if the path leads to a directory
        elif os.path.isdir(file_path):
            files = os.listdir(file_path)
            # parse each file in the directory
            for file in files:
                translated = parse_file(assembler, os.path.abspath(os.path.join(file_path, file)))
                create_output_file(os.path.splitext(file)[0], translated)


def main():
    if len(sys.argv) == 2:
        file_path = sys.argv[1]
        assembler = Assembler()
        check_input(assembler, file_path)


if __name__ == '__main__':
    main()
