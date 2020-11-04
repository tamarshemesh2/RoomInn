import os
import re
import sys

from Code import Code
from Parser import Parser, CommandTypes
from SymbolTable import SymbolTable

# ######################################### MAGIC_NUMBERS ############################################
VARIABLE_REGEX = '@.+'
LABEL_REGEX = '\\(.+\\)'
VARIABLE_START = "@"
PARENTHESIS = '[()]'
EMPTY = ''
C_START = "111"
DIGIT_REGEX = '[\\d]+'
TO_16_BIT = "{0:016b}"
COMMENT = "//"
COMP_START = 0
COMP_REST = 1
OUTPUT_EXTENSION = ".hack"
NUM_OF_ARGS = 2
INPUT_FILE_INDEX = 1
SHIFT_FILL = "0000"
# ####################################################################################################


class Assembler:
    def __init__(self):
        self._symbol_table = SymbolTable()

    def parse_labels_and_vars(self, file_lines):
        without_labels = []
        for line in file_lines:
            # check if is variable i.e. @ then not just numbers
            is_variable = re.match(VARIABLE_REGEX, line) and not re.match(DIGIT_REGEX, line.split(VARIABLE_START)[1])
            is_label = re.match(LABEL_REGEX, line)
            if bool(is_variable):
                # adds variable to the symbol table
                self._symbol_table.add_entry(line.split(VARIABLE_START)[1], self._symbol_table.get_counter())
            elif bool(is_label):
                # adds label to the symbol table
                self._symbol_table.add_entry(re.sub(PARENTHESIS, EMPTY, line), self._symbol_table.get_counter())
                continue
            without_labels.append(line.split(COMMENT)[0])
        return without_labels

    def parse_instructions(self, instructions):
        parser = Parser(instructions)
        code = Code()
        translated = []
        while parser.has_more_commands():
            command_type = parser.command_type()
            # C instruction
            if command_type == CommandTypes.C:
                translate_c_instruction(parser, code, translated)
            # A instruction
            elif command_type == CommandTypes.A:
                self.translate_a_instruction(parser, translated)
            # shift command
            elif command_type == CommandTypes.Shift:
                translate_shift_command(parser, code, translated)
            parser.advance()
        return translated

    def translate_a_instruction(self, parser, translated):
        parsed_symbol = parser.symbol()
        is_digit = re.match(DIGIT_REGEX, parsed_symbol)
        if is_digit:
            translated.append(str(TO_16_BIT.format(int(parsed_symbol))))
        else:
            if self._symbol_table.contains(parsed_symbol):
                translated.append(str(TO_16_BIT.format(self._symbol_table.get_address(parsed_symbol))))


def translate_c_instruction(parser, code, translated):
    comp_field = code.comp(parser.comp())
    dest_field = code.dest(parser.dest())
    jump_field = code.jump(parser.jump())
    current_translated = C_START + comp_field + dest_field + jump_field
    translated.append(current_translated)


def translate_shift_command(parser, code, translated):
    shift_field = parser.shift()
    comp_field = parser.comp()
    dest_field = parser.dest()
    jump_field = parser.jump()
    # takes only needed fields of comp not overridden by shift
    current_translated = shift_field + SHIFT_FILL + code.dest(dest_field) + code.jump(jump_field)
    translated.append(current_translated)


def parse_file(assembler, filename):
    with open(filename, 'r') as infile:
        asm_lines = []
        for line in infile:
            # skip empty lines
            if len(line.split()) == 0 or line.startswith(COMMENT):
                continue
            else:
                asm_lines.append(EMPTY.join(line.split()))
        without_labels = assembler.parse_labels_and_vars(asm_lines)
        translated = assembler.parse_instructions(without_labels)
    infile.close()
    return translated


def create_output_file(filename, translated_lines):
    output_filename = filename + OUTPUT_EXTENSION
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
    if len(sys.argv) == NUM_OF_ARGS:
        file_path = sys.argv[INPUT_FILE_INDEX]
        assembler = Assembler()
        check_input(assembler, file_path)


if __name__ == '__main__':
    main()
